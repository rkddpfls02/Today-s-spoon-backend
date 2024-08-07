package com.example.todaySpoon.controller;


import com.example.todaySpoon.Service.FoodService;
import com.example.todaySpoon.entity.EatenFood;
import com.example.todaySpoon.entity.Food;
import com.example.todaySpoon.repository.FoodRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Tag(name="기록페이지 api", description = "모든 음식 리스트 조회 기능, 음식 기록 기능")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FoodController {


    private final FoodRepository foodRepository;
    private static final String CSV_FILE_PATH = "C:\\Users\\User\\Desktop\\food.csv";
    private static final String DB_URL = "jdbc:mysql://todayspoon.cjimkm6ow1mn.ap-northeast-2.rds.amazonaws.com:3306/todaySpoon?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "shsh0615";

    private final FoodService foodService;


    // 음식 리스트 다 보여주기
    @Operation(summary = "모든 음식 리스트 조회", description = "사용자가 기록페이지에서 먹은 음식을 기록하기 위해 모든 음식 리스트를 조회합니다")
    @GetMapping("/foods")
    public List<Food> getFoodList() {
        return foodService.getFoodList();
    }

    @Operation(summary = "음식 아이디로 음식 조회", description = "기록페이지에서 먹은 음식을 선택하면, 선택한 음식의 id받아 food class를 반환합니다.")
    @GetMapping("/foods/{food-id}")
    public Optional<Food> getFood(@PathVariable("food-id") Long foodId){return foodService.getById(foodId);}
    // 음식 기록하기
    @Operation(summary = "먹은 음식 기록", description = "사용자가 먹은 음식을 기록합니다. 음식아이디, 유저아이디, 양을 반환")
    @PostMapping("/{amount}/{userId}/{foodId}")
    public EatenFood addFood(@PathVariable Long foodId, @PathVariable String userId, @PathVariable float amount){
        return foodService.saveFood(foodId,userId,amount);
    }



//    // 이미지 불러오기 파일명으로 url을 찾아오는 형식.
//    @GetMapping("/image")
//    public ResponseEntity<String> getFileUrl(@RequestParam("fileName") String fileName) {
//        try {
//            String fileUrl = s3Uploader.getFileUrl(fileName);
//            return ResponseEntity.ok(fileUrl);
//        } catch (S3Uploader.Exception400 e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (S3Uploader.Exception500 e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }


    @Operation(summary = "음식 리스트를 데이터베이스에 저장합니다", description = "프론트가 사용하지 않습니다")
    @GetMapping("/aaaa")
    public void addfood() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {

            String[] nextLine;
//            String insertQuery = "INSERT INTO food (foodid, food_name, unit_amount, carbohydrate_amount,fat_amount, protein_amount, calorie) VALUES (?, ?, ?,?,?,?,?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            csvReader.readNext();

            while ((nextLine = csvReader.readNext()) != null) {
                Food food = new Food();
                food.setFoodName(nextLine[0]);
                System.out.println(food.getFoodName());
                food.setProteinAmount(Float.parseFloat(nextLine[1]));
                food.setFatAmount(Float.parseFloat(nextLine[2]));
                food.setCarbohydrateAmount(Float.parseFloat(nextLine[3]));
                food.setCalorie(Float.parseFloat(nextLine[4]));
                food.setUnitAmount(Float.parseFloat(nextLine[5]));

                foodRepository.save(food);
            }


        } catch (SQLException | CsvValidationException | IOException e) {
            e.printStackTrace();
        }
    }


}


