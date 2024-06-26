package com.Manager.Hotel.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.Instant;

import com.Manager.Hotel.repository.BookingRepository;
import com.Manager.Hotel.repository.RoomRepository;
import com.Manager.Hotel.repository.CustomerRepository;
import com.Manager.Hotel.repository.HotelRepository;
import com.Manager.Hotel.repository.CouponRepository;
import com.Manager.Hotel.entity.Booking;
import com.Manager.Hotel.entity.Customer;
import com.Manager.Hotel.entity.Hotel;
import com.Manager.Hotel.entity.Room;
import com.Manager.Hotel.entity.Coupon;

@Service
public class MainService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CouponRepository couponRepository;
  
    public Customer createAccount(Customer customer) {  
        return customerRepository.save(customer);
    }

    public Customer login(Customer customer) {
        return customerRepository.findByEmailAndPassword(customer.getEmail(), customer.getPassword());
    }

    public List<Hotel> getHotels() {
        return hotelRepository.findAll();
    }

    public List<Room> getRoomsByHotelID(Long hotelID) {
        return roomRepository.findByHotelId(hotelID);
    }

    public List<Room> getRoomsByAvailability(Boolean availability, Long hotelId) {
        return roomRepository.findByAvailabilityAndHotelId(availability, hotelId);
    }

    public Booking makeBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Room updateRoomAvailability(Long roomID, Boolean availability) {
        Room room = roomRepository.findById(roomID).orElse(null);
        if (room != null) {
            room.setAvailability(availability);
            return roomRepository.save(room);
        }
        return null;
    }


    public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
    }

    public boolean checkAvailability(Long roomId, LocalDate startDate, int noOfDays) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            LocalDate checkOut = startDate.plusDays(noOfDays);
            List<Booking> bookings = bookingRepository.findByRoomId(roomId);
            for (Booking booking : bookings) {
                System.out.println(booking.getStartDate());
                System.out.println(booking.getNoOfDays());
                LocalDate existingStart = convertToLocalDateViaMilisecond(booking.getStartDate());
                LocalDate existingEnd = existingStart.plusDays(booking.getNoOfDays());

                if ((existingStart.isBefore(checkOut) && existingEnd.isAfter(startDate)) ||
                    existingStart.equals(startDate) ||
                    existingEnd.equals(checkOut)) {
                    return false;  
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkCouponValidity(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        
        if (coupon != null) {
            System.out.println(coupon.getCode());
            return true;
        }
        return false;
    }

        
    


    public Booking updateBookingCheckInStatus(Long id, Boolean checkInStatus) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setCheckInStatus(checkInStatus);
            return bookingRepository.save(booking);
        }
        return null;
    }

    public Booking updateBookingCheckOutStatus(Long id, Boolean checkOutStatus) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setCheckOutStatus(checkOutStatus);
            return bookingRepository.save(booking);
        }
        return null;
    }

    public Booking updateBookingPaymentStatus(Long id, Boolean paymentStatus) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking != null) {
            booking.setPaymentStatus(paymentStatus);
            return bookingRepository.save(booking);
        }
        return null;
    }

    public List<Booking> getBookingsByCustomerID(Long customerID) {
        return bookingRepository.findByCustomerId(customerID);
    }

    public Customer setSubcriptionCustomer(Long customerID, Boolean sub) {
        Customer customer = customerRepository.findById(customerID).orElse(null);
        if (customer != null) {
            customer.setSub(sub);
            System.out.println(customer.isSubscribed());
            return customerRepository.save(customer);
        }
        return customer;
    }

    public Coupon getCoupon(String code) {
        return couponRepository.findByCode(code);
    }

}
