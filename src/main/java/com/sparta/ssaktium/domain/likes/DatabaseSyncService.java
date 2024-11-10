package com.sparta.ssaktium.domain.likes;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.Set;

@Component
public class DatabaseSyncService {

    private final LikeRedisService redisService;

    public DatabaseSyncService(LikeRedisService redisService) {
        this.redisService = redisService;
    }

    @Scheduled(fixedRate = 60000)  // 60초마다 실행
    public void syncLikesToDB() {
        System.out.println("스케쥴링 시작");

        try (Jedis jedis = new Jedis("localhost", 6379);
             Connection dbConnection = DriverManager.getConnection(
                     "jdbc:mysql://localhost:3306/ssak_tium",
                     "root",
                     System.getenv("DB_PASSWORD"))) {

            System.out.println("Redis와 MySQL 연결 성공");

            // 게시글과 댓글 각각 좋아요 데이터 동기화
            syncLikes("Board", jedis, dbConnection);
            syncLikes("Comment", jedis, dbConnection);

            System.out.println("스케쥴링 완료");
        } catch (SQLException exception) {
            System.out.println("DB 연결 또는 쿼리 실행 에러 발생");
            exception.printStackTrace();
        }
    }

    private void syncLikes(String type, Jedis jedis, Connection dbConnection) throws SQLException {
        // Redis에서 사용자별 좋아요 데이터를 관리하는 키 패턴
        String redisUserLikePattern = type + "_likes:*";

        // 좋아요 수를 업데이트할 DB 쿼리 설정
        String countUpdateQuery = type.equals("Board") ?
                "UPDATE boards SET board_likes_count = board_likes_count + ? WHERE id = ?" :
                "UPDATE comments SET comment_likes_count = comment_likes_count + ? WHERE id = ?";

        // Redis에서 모든 좋아요 키를 조회
        Set<String> keys = jedis.keys(redisUserLikePattern);
        System.out.println(type + "에서 찾은 Redis 사용자별 좋아요 키: " + keys);

        for (String key : keys) {
            String id = key.split(":")[1];  // 게시글 또는 댓글 ID 추출
            Set<String> redisUserIds = jedis.smembers(key);  // 해당 항목에 좋아요를 누른 유저 ID 조회
            int likesCount = redisUserIds.size();  // Redis에 저장된 좋아요 수

            System.out.println(type + " 좋아요 수 (ID: " + id + "): " + likesCount + ", 유저 목록: " + redisUserIds);

            // 좋아요 수를 DB에 업데이트
            incrementLikesCountInDB(dbConnection, id, likesCount, countUpdateQuery);

            // 새로운 좋아요 기록을 DB에 삽입
            insertUserLikeRecords(dbConnection, id, redisUserIds, type.equals("Board") ? "board_likes" : "comment_likes");
        }
    }

    private void incrementLikesCountInDB(Connection dbConnection, String id, int likesCount, String query) throws SQLException {
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, likesCount); // 증가시킬 좋아요 수
            stmt.setString(2, id);      // 게시글 또는 댓글 ID
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("좋아요 수 업데이트 완료 (" + id + "), 업데이트된 행 수: " + rowsUpdated);
        }
    }

    private void insertUserLikeRecords(Connection dbConnection, String id, Set<String> userIds, String table) throws SQLException {
        // 게시글인지 댓글인지에 따라 적절한 컬럼을 선택합니다.
        String query = "INSERT INTO " + table + " (user_id, " +
                (table.equals("board_likes") ? "board_id" : "comment_id") + ") " +
                "VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE user_id = user_id";  // 중복 시 UPDATE는 사실상 아무 것도 변경하지 않음

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            for (String userId : userIds) {
                stmt.setString(1, userId); // 유저 ID
                stmt.setString(2, id);      // 게시글 또는 댓글 ID
                stmt.addBatch();            // 배치 모드로 다수의 삽입 처리
            }

            // 배치 실행
            int[] result = stmt.executeBatch();
            System.out.println("좋아요 기록 삽입 완료 (" + id + "), 삽입된 행 수: " + result.length);
        }
    }
}
