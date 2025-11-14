package com.company.myappapi.util;



import com.company.myappapi.exception.GenerateException;
import com.company.myappapi.filter.FilterOperation;
import com.company.myappapi.filter.FilterSortRequest;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.tika.Tika;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    private static final Tika tika = new Tika();
    private static final DateTimeFormatter fmDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter fmDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static String generateUserPassword() {
        var charData = new CharacterData() {
            @Override
            public String getErrorCode() {
                return "SPEC_ERROR_CODE";
            }

            @Override
            public String getCharacters() {
                return "!@#$%&*()";
            }
        };




        var passwordRules = Arrays.asList(
                new CharacterRule(EnglishCharacterData.UpperCase),
                new CharacterRule(EnglishCharacterData.LowerCase),
                new CharacterRule(EnglishCharacterData.Digit),
                new CharacterRule(charData)
        );

        return new PasswordGenerator().generatePassword(12, passwordRules);
    }



    public static String detectFileExtension(MultipartFile file, String fileName) {
        String type = "";
        try {
            type = tika.detect(file.getBytes(), fileName);
        } catch (IOException ignored) {
        }

        return type;
    }


    public static List<FilterSortRequest> filterSortRequest(List<String> filterFields, List<String> filterOperations, List<String> filterValues) {
        List<FilterSortRequest> filterSortRequests = new ArrayList<>();

        if (filterFields != null && filterOperations != null && filterValues != null) {
            for (int i = 0; i < filterFields.size(); i++) {
                filterSortRequests.add(new FilterSortRequest(filterFields.get(i),
                        FilterOperation.valueOf(filterOperations.get(i)),
                        filterValues.get(i)));
            }
        }

        return filterSortRequests;

    }

    public static LocalDateTime startOfTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
    }

    public static LocalDateTime endOfTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.of(23, 59, 59, 0));
    }

    public static String formatNameSurname(String name, String surname) {
        return name != null && surname != null ? name.toLowerCase() + "." + surname.toLowerCase() : "";
    }


    public static ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        var time = new JavaTimeModule();

        time.addSerializer(LocalDate.class, new LocalDateSerializer(fmDate));

        mapper.registerModule(time);
        mapper.disable(MapperFeature.USE_ANNOTATIONS);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        return mapper;
    }

    public static String toJson(Object object) {
        try {
            return objectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new GenerateException("error.msg");
        }
    }
}
