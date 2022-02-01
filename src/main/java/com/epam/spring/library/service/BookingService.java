package com.epam.spring.library.service;

import com.epam.spring.library.dto.BookDTO;
import com.epam.spring.library.dto.BookingDTO;

import java.util.List;

public interface BookingService {

    BookingDTO getBooking(int id);

    List<BookingDTO> getAllBookings();

    BookingDTO createBooking(BookingDTO bookingDTO);

    BookingDTO updateBooking(int id, BookingDTO bookingDTO);

    void deleteBooking(int id);

    List<BookDTO> addBookToBooking(int id, List<BookDTO> bookDTOs);

    void deleteBookFromBooking(int id, String isbn);

    List<BookDTO> getBooksInBooking(int id);

    List<BookDTO> updateBooksInBooking(int id, List<BookDTO> bookDTOs);

    void clearBookListInBooking(int id);
}