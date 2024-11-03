package com.ssafy.c107.main.domain.review.repository;

import com.ssafy.c107.main.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByOrderList_Id(Long id);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.orderList ol " +
            "JOIN FETCH ol.order o " +
            "JOIN FETCH o.store s " +
            "WHERE s.id = :storeId")
    List<Review> findAllByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.orderList ol " +
            "JOIN FETCH ol.order o " +
            "JOIN FETCH ol.food f " +
            "WHERE o.store.id = :storeId")
    List<Review> findReviewByStoreId(@Param("storeId") Long storeId);

    List<Review> findAllByOrderList_Food_Id(Long id);
}
