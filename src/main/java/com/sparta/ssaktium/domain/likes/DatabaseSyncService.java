package com.sparta.ssaktium.domain.likes;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.Set;

@Component
public class DatabaseSyncService {

    private final LikeRedisService likeRedisService;
    private final RedisTemplate<String, String> redisTemplate;

    public DatabaseSyncService(LikeRedisService redisService,RedisTemplate<String, String> redisTemplate) {
        this.likeRedisService = redisService;
        this.redisTemplate = redisTemplate;
    }

    // Redis에 있는 좋아요를 DB 에 저장
    @Scheduled(fixedRate = 60000)  // 60초마다 실행
    public void syncLikesToDB() {
        System.out.println("스케쥴링 시작");
        // 환경 변수 .env에서 가져오는거 실패해서 따로 등록함 (팀원들에게 공유 필요)
        try (Connection dbConnection = DriverManager.getConnection(
                     "jdbc:mysql://localhost:3306/ssak_tium",
                     "root",
                     System.getenv("DB_PASSWORD"))) {

            System.out.println("Redis와 MySQL 연결 성공");

            // 게시글과 댓글 각각 좋아요 데이터 동기화
            syncLikes(likeRedisService.TARGET_TYPE_BOARD, dbConnection);
            syncLikes(likeRedisService.TARGET_TYPE_COMMENT, dbConnection);

            System.out.println("스케쥴링 완료");
        } catch (SQLException exception) {
            System.out.println("DB 연결 또는 쿼리 실행 에러 발생");
            exception.printStackTrace();
        }
    }

    // 핵심! type : 게시글이냐 댓글이냐 / jedis : redis 객체 / dbConnection : MySQL과 연결 담당 객체
    private void syncLikes(String type,
                           Connection dbConnection) throws SQLException {
        // Redis에서 사용자별 좋아요 데이터를 관리하는 키 패턴
        String redisUserLikePattern = type + "_likes:*";

        // 좋아요 수를 업데이트할 DB 쿼리 설정
        String countUpdateQuery = type.equals(likeRedisService.TARGET_TYPE_BOARD) ?
                "UPDATE boards SET board_likes_count = board_likes_count + ? WHERE id = ?" :
                "UPDATE comments SET comment_likes_count = comment_likes_count + ? WHERE id = ?";

        // Redis에서 모든 좋아요 키를 조회
        Set<String> keys = redisTemplate.keys(redisUserLikePattern);
        System.out.println(type + "에서 찾은 Redis 사용자별 좋아요 키: " + keys);

        for (String key : keys) {
            String id = key.split(":")[1];  // 게시글 또는 댓글 ID 추출
            Set<String> redisUserIds = redisTemplate.opsForSet().members(key);  // 해당 항목에 좋아요를 누른 유저 ID 조회
            int likesCount = redisUserIds.size();  // Redis에 저장된 좋아요 수

            int currentLikesCountInDB = getCurrentLikesCountFromDB(dbConnection, id, type);

            if (likesCount > currentLikesCountInDB) {
                System.out.println(type + " 좋아요 수 (ID: " + id + "): " + likesCount + ", 유저 목록: " + redisUserIds);

                // 좋아요 수를 DB에 업데이트
                incrementLikesCountInDB(dbConnection, id, likesCount, countUpdateQuery);

                // 새로운 좋아요 기록을 DB에 삽입
                insertUserLikeRecords(
                        dbConnection,
                        id,
                        redisUserIds,
                        type.equals(likeRedisService.TARGET_TYPE_BOARD) ? "board_likes" : "comment_likes");
            } else {
                System.out.println("동기화 필요 없음.");
            }
        }
    }

    @Transactional
    public void incrementLikesCountInDB(Connection dbConnection,
                                        String id,
                                        int likesCount,
                                        String query) throws SQLException {
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, likesCount); // 증가시킬 좋아요 수
            stmt.setString(2, id);      // 게시글 또는 댓글 ID
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("좋아요 수 업데이트 완료 (" + id + "), 업데이트된 행 수: " + rowsUpdated);
        }
    }

    @Transactional
    public void insertUserLikeRecords(Connection dbConnection,
                                      String id,
                                      Set<String> userIds,
                                      String table) throws SQLException {
        // 게시글인지 댓글인지에 따라 적절한 컬럼을 선택합니다.
        String query = "INSERT IGNORE INTO " + table + " (user_id, " +
                (table.equals("board_likes") ? "board_id" : "comment_id") + ") " +
                "VALUES (?, ?)";  // 중복된 데이터는 삽입되지 않음

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

    // DB에서 현재 좋아요 수를 조회하는 메서드
    private int getCurrentLikesCountFromDB(Connection dbConnection,
                                           String id,
                                           String type) throws SQLException {
        String countQuery = type.equals(likeRedisService.TARGET_TYPE_BOARD) ?
                "SELECT board_likes_count FROM boards WHERE id = ?" :
                "SELECT comment_likes_count FROM comments WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(countQuery)) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0; // 만약 DB에 레코드가 없다면 좋아요 수는 0으로 간주
            }
        }
    }
}
