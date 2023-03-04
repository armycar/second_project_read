package com.readers.be3;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.readers.be3.entity.BookInfoEntity;
import com.readers.be3.entity.UserInfoEntity;
import com.readers.be3.repository.BookInfoRepository;
import com.readers.be3.repository.UserInfoRepository;
import com.readers.be3.service.UserInfoService;
import com.readers.be3.utilities.RandomNameUtils;

@SpringBootTest
class Be3ApplicationTests {
	@Autowired UserInfoRepository userInfoRepository;
	@Autowired BookInfoRepository bookInfoRepository;
	@Autowired UserInfoService userInfoService;

	@Test
	void contextLoads() {
	}

	@Test
	void testSearchBooks() {
		// System.out.println(bookInfoRepository.findByBiNameContains("생에").get(0).getBiName());
	}

	@Test
	void testLocalDate() {
		LocalDate date1 = LocalDate.of(2023, 03, 20);
		LocalDate date2 = LocalDate.of(2020, 01, 01);
		LocalDate date3 = LocalDate.of(2020, 01, 01);
		LocalDate date4 = LocalDate.of(2010, 01, 01);

		System.out.println("isAfter : " + date1.isAfter(LocalDate.now()));
		System.out.println("isAfter : " + date4.isAfter(date2));
		System.out.println("isAfter : " + date3.isAfter(date2));
		System.out.println("isAfter : " + date1.isAfter(date2));
		System.out.println("isBefore : " + date1.isBefore(date2));
		System.out.println("isBefore : " + date3.isBefore(date2));

		System.out.println("plusDays : " + date1.plusDays(1));
	}

	@Test
	void testSNSLogin() {
		// SnsLoginRequest request = new SnsLoginRequest("test123", "kakao");
		// userInfoService.snsloginUser(request);
	}

	@Test
	void testRandomName() {
		// String type = "read";
		// System.out.println(Integer.parseInt(type, 32));
		// String randName = RandomNameUtils.MakeRandomUri("update", 146L);
		System.out.println(RandomNameUtils.MakeRandomUri("update", 146L));
		System.out.println(RandomNameUtils.MakeRandomUri("jpg", 146L));
		System.out.println(RandomNameUtils.MakeRandomUri("png", 146L));
		System.out.println(RandomNameUtils.MakeRandomUri("gif", 146L));
	}

	@Test
	void testUserInfoUpdate() {
		UserInfoEntity uEntity = userInfoRepository.findById(1L).get();
        BookInfoEntity bEntity = bookInfoRepository.findById(2L).get();

		Integer totalBook = uEntity.getUiTotalBook();
        Integer totalPage = uEntity.getUiTotalPage();
		System.out.println("totalBook : " + totalBook + ", totalPage : " + totalPage);
		
		uEntity = UserInfoEntity.builder().uiTotalBook(totalBook+1).uiTotalPage(totalPage+bEntity.getBiPage()).build();
		System.out.println("totalBook : " + totalBook + ", totalPage : " + totalPage);
		userInfoRepository.save(uEntity);
	}
}
