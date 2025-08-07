package com.porikroma.repository;

import com.porikroma.model.Trip;
import com.porikroma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    List<Trip> findByUser(User user);
    
    List<Trip> findByUserOrderByStartDateDesc(User user);
    
    List<Trip> findByTripStatus(Trip.TripStatus status);
    
    List<Trip> findByUserAndTripStatus(User user, Trip.TripStatus status);
    
    @Query("SELECT t FROM Trip t WHERE t.user = :user AND t.startDate >= :startDate AND t.endDate <= :endDate")
    List<Trip> findByUserAndDateRange(@Param("user") User user, 
                                     @Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
    
    @Query("SELECT t FROM Trip t WHERE t.user = :user AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.destination) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Trip> searchUserTrips(@Param("user") User user, @Param("searchTerm") String searchTerm);
    
    List<Trip> findByTravelGroupId(Long groupId);
    
    @Query("SELECT t FROM Trip t WHERE t.isGroupTrip = false AND t.user = :user")
    List<Trip> findSoloTripsByUser(@Param("user") User user);
    
    @Query("SELECT t FROM Trip t WHERE t.isGroupTrip = true AND t.user = :user")
    List<Trip> findGroupTripsByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.user = :user AND t.tripStatus = 'COMPLETED'")
    Long countCompletedTripsByUser(@Param("user") User user);
}