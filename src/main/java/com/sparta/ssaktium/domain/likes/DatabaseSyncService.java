package com.sparta.ssaktium.domain.likes;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

@Component
public class DatabaseSyncService {

    private final LikeRedisService redisService;

    public DatabaseSyncService(LikeRedisService redisService) {
        this.redisService = redisService;
    }

    @Scheduled(fixedRate = 60000)  // 60초마다 실행
    public void syncLikesToDB() {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            // 게시글 좋아요 처리
            Set<String> postKeys = jedis.keys("likes:post:*");  // Redis에서 "likes:post:*"로 시작하는 모든 키를 조회
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssak_tium", "root", "DB_PASSWORD");

            // 게시글 좋아요 처리
            for (String key : postKeys) {
                String postId = key.split(":")[2];  // 게시글 ID
                Set<String> userIds = jedis.smembers(key);  // 게시글에 좋아요를 누른 유저들 조회

                int likesCount = userIds.size();  // 유저 수를 좋아요 수로 간주
                updatePostLikesInDB(dbConnection, postId, likesCount);

                // 게시글 좋아요 기록 처리
                for (String userId : userIds) {
                    if (!hasUserLikedPost(dbConnection, postId, userId)) {
                        insertUserLikeRecord(dbConnection, postId, userId, "post");
                    }
                }
            }

            // 댓글 좋아요 처리
            Set<String> commentKeys = jedis.keys("likes:comment:*");  // Redis에서 "likes:comment:*"로 시작하는 모든 키를 조회
            for (String key : commentKeys) {
                String commentId = key.split(":")[2];  // 댓글 ID
                Set<String> userIds = jedis.smembers(key);  // 댓글에 좋아요를 누른 유저들 조회

                int likesCount = userIds.size();  // 유저 수를 좋아요 수로 간주
                updateCommentLikesInDB(dbConnection, commentId, likesCount);

                // 댓글 좋아요 기록 처리
                for (String userId : userIds) {
                    if (!hasUserLikedComment(dbConnection, commentId, userId)) {
                        insertUserLikeRecord(dbConnection, commentId, userId, "comment");
                    }
                }
            }

            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 게시글의 좋아요 수를 DB에 업데이트하는 메서드
    private void updatePostLikesInDB(Connection dbConnection, String postId, int likesCount) throws SQLException {
        String query = "UPDATE posts SET likes = ? WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, likesCount);  // Redis에서 가져온 좋아요 수를 DB에 업데이트
            stmt.setString(2, postId);   // 게시물 ID
            stmt.executeUpdate();
        }
    }

    // 댓글의 좋아요 수를 DB에 업데이트하는 메서드
    private void updateCommentLikesInDB(Connection dbConnection, String commentId, int likesCount) throws SQLException {
        String query = "UPDATE comments SET likes = ? WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, likesCount);  // Redis에서 가져온 좋아요 수를 DB에 업데이트
            stmt.setString(2, commentId);   // 댓글 ID
            stmt.executeUpdate();
        }
    }

    // 특정 유저가 해당 게시글에 좋아요를 눌렀는지 확인하는 메서드 (게시글)
    private boolean hasUserLikedPost(Connection dbConnection, String postId, String userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM post_likes WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, postId);
            stmt.setString(2, userId);
            var resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;  // 이미 좋아요를 눌렀으면 true
        }
    }

    // 특정 유저가 해당 댓글에 좋아요를 눌렀는지 확인하는 메서드 (댓글)
    private boolean hasUserLikedComment(Connection dbConnection, String commentId, String userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM comment_likes WHERE comment_id = ? AND user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, commentId);
            stmt.setString(2, userId);
            var resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;  // 이미 좋아요를 눌렀으면 true
        }
    }

    // 유저의 좋아요 기록을 DB에 저장하는 메서드 (게시글/댓글 공통)
    private void insertUserLikeRecord(Connection dbConnection, String id, String userId, String type) throws SQLException {
        String table = (type.equals("post")) ? "post_likes" : "comment_likes";
        String query = "INSERT INTO " + table + " (id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, id);     // 게시글 ID 또는 댓글 ID
            stmt.setString(2, userId); // 유저 ID
            stmt.executeUpdate();      // 좋아요 기록 삽입
        }
    }
}