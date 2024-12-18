package com.ssafy.c107.main.domain.elasticsearch.service;

import com.ssafy.c107.main.common.aws.FileService;
import com.ssafy.c107.main.domain.elasticsearch.dto.FoodInfoDetail;
import com.ssafy.c107.main.domain.elasticsearch.dto.SearchDetail;
import com.ssafy.c107.main.domain.elasticsearch.dto.response.SearchResponse;
import com.ssafy.c107.main.domain.elasticsearch.entity.StoreSearchDocument;
import com.ssafy.c107.main.domain.elasticsearch.entity.StoreSearchDocument.FoodInfo;
import com.ssafy.c107.main.domain.elasticsearch.repository.StoreSearchRepository;
import com.ssafy.c107.main.domain.food.entity.Food;
import com.ssafy.c107.main.domain.store.entity.Store;
import com.ssafy.c107.main.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private final StoreSearchRepository storeSearchRepository;
    private final StoreRepository storeRepository;
    private final FileService fileService;
//    @EventListener(ApplicationReadyEvent.class)
//    @Transactional
//    void postConstruct() {
//        if (isDevelopmentEnvironment()) { // 개발 환경에서만 실행
//            storeSearchRepository.deleteAll();
//            initializeStoreData();
//        }
//    }
//
//    private boolean isDevelopmentEnvironment() {
//        return "dev".equals(System.getProperty("spring.profiles.active"));
//    }
//
//    private void initializeStoreData() {
//        List<Store> stores = storeRepository.findFoods();
//        for (Store store : stores) {
//            List<Food> foods = store.getFoods();
//            List<FoodInfo> foodInfo = foods.stream().map(food -> FoodInfo.builder()
//                            .foodName(food.getName())
//                            .price(food.getPrice())
//                            .description(food.getDescription())
//                            .build())
//                    .toList();
//
//            StoreSearchDocument document = StoreSearchDocument.builder()
//                    .id(store.getId())
//                    .storeName(store.getName())
//                    .address(store.getAddress())
//                    .description(store.getDescription())
//                    .img(store.getImg())
//                    .foods(foodInfo)
//                    .build();
//
//            storeSearchRepository.save(document);
//        }
//    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    void postConstruct() {
        storeSearchRepository.deleteAll();

        List<Store> stores = storeRepository.findFoods();
        for (Store store : stores) {
            List<Food> foods = store.getFoods();
            List<FoodInfo> foodInfo = foods.stream().map(food -> FoodInfo.builder()
                            .foodName(food.getName())
                            .price(food.getPrice())
                            .description(food.getDescription())
                            .build())
                    .toList();

            StoreSearchDocument document = StoreSearchDocument.builder()
                    .id(store.getId())
                    .storeName(store.getName())
                    .address(store.getAddress())
                    .description(store.getDescription())
                    .img(store.getImg())
                    .foods(foodInfo)
                    .build();

            storeSearchRepository.save(document);
        }
    }

    @Override
    public SearchResponse getStores(String query) {
        Pageable pageable = PageRequest.of(0, 100);

        Page<StoreSearchDocument> searchResult = storeSearchRepository.findByStoreNameOrFoodName(
                query, pageable);

        List<StoreSearchDocument> stores = searchResult.getContent();

        List<SearchDetail> results = new ArrayList<>();

        for (StoreSearchDocument store : stores) {
            results.add(SearchDetail
                    .builder()
                    .storeId(store.getId())
                    .storeAddress(store.getAddress())
                    .storeDescription(store.getDescription())
                    .storeImg(store.getImg())
                    .storeName(store.getStoreName())
                    .foods(store.getFoods().stream().map(food -> FoodInfoDetail
                            .builder()
                            .foodDescription(food.getDescription())
                            .foodPrice(food.getPrice())
                            .foodName(food.getFoodName())
                            .build()).toList())
                    .build());
        }
        return SearchResponse
                .builder()
                .stores(results)
                .build();
    }
}
