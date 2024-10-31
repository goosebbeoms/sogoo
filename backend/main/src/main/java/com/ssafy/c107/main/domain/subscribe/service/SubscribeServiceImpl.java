package com.ssafy.c107.main.domain.subscribe.service;

import com.ssafy.c107.main.common.entity.WeeklyFood;
import com.ssafy.c107.main.domain.food.dto.FoodDto;
import com.ssafy.c107.main.domain.food.entity.Food;
import com.ssafy.c107.main.domain.food.exception.FoodNotFoundException;
import com.ssafy.c107.main.domain.food.repository.FoodRepository;
import com.ssafy.c107.main.domain.members.exception.InvalidMemberRoleException;
import com.ssafy.c107.main.domain.store.entity.Store;
import com.ssafy.c107.main.domain.store.exception.StoreNotFoundException;
import com.ssafy.c107.main.domain.store.repository.StoreRepository;
import com.ssafy.c107.main.domain.subscribe.dto.SubscribeDetailDto;
import com.ssafy.c107.main.domain.subscribe.dto.SubscribeDetailWeekDto;
import com.ssafy.c107.main.domain.subscribe.dto.SubscribeWeekDto;
import com.ssafy.c107.main.domain.subscribe.dto.request.AppendSubscribeRequest;
import com.ssafy.c107.main.domain.subscribe.dto.response.GetSubscribeResponse;
import com.ssafy.c107.main.domain.subscribe.dto.response.SubscribeDetailResponse;
import com.ssafy.c107.main.domain.subscribe.entity.Subscribe;
import com.ssafy.c107.main.domain.subscribe.entity.SubscribeWeek;
import com.ssafy.c107.main.domain.subscribe.exception.SubscribeNotFoundException;
import com.ssafy.c107.main.domain.subscribe.repository.SubscribeRepository;
import com.ssafy.c107.main.domain.subscribe.repository.SubscribeWeekRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final StoreRepository storeRepository;
    private final FoodRepository foodRepository;
    private final SubscribeWeekRepository subscribeWeekRepository;

    @Transactional(readOnly = true)
    public GetSubscribeResponse getSubscribe(Long id) {
        // 조인 쿼리를 사용해 구독 상품과 연관된 모든 데이터를 한 번에 가져옴.
        Subscribe subscribe = subscribeRepository.findSubscribeWithDetails(id)
                .orElseThrow(SubscribeNotFoundException::new);

        // SubscribeDetailDto 생성 및 기본 정보 설정
        SubscribeDetailDto subscribeDetailDto = new SubscribeDetailDto();
        subscribeDetailDto.setSubscribeId(subscribe.getId());
        subscribeDetailDto.setSubscribeName(subscribe.getName());
        subscribeDetailDto.setSubscribePrice(subscribe.getPrice());
        subscribeDetailDto.setSubscribeDescription(subscribe.getDescription());
        subscribeDetailDto.setSubscribeRate(subscribe.getRate());

        // 주차별 구독 정보 설정
        List<SubscribeWeekDto> subscribeWeeks = subscribe.getSubscribeWeeks()
                .stream()
                .map(subscribeWeek -> {
                    SubscribeWeekDto subscribeWeekDto = new SubscribeWeekDto();
                    subscribeWeekDto.setSubscribeDate(subscribeWeek.getDate().toString());
                    subscribeWeekDto.setSubscribeRound(subscribeWeek.getRound());

                    // 주차별 반찬 정보 설정
                    List<FoodDto> foodDtos = subscribeWeek.getWeeklyFoods()
                            .stream()
                            .map(weeklyFood -> {
                                FoodDto foodDto = new FoodDto();
                                foodDto.setFoodId(weeklyFood.getFood().getId());
                                foodDto.setFoodName(weeklyFood.getFood().getName());
                                foodDto.setFoodDescription(weeklyFood.getFood().getDescription());
                                foodDto.setFoodImg(weeklyFood.getFood().getImg());
                                return foodDto;
                            }).collect(Collectors.toList());

                    subscribeWeekDto.setFoods(foodDtos);
                    return subscribeWeekDto;
                }).collect(Collectors.toList());

        // 주차별 구독 정보 리스트 설정
        subscribeDetailDto.setWeeklyFood(subscribeWeeks);

        // 최종 응답 Dto 생성
        GetSubscribeResponse getSubscribeResponse = new GetSubscribeResponse();
        getSubscribeResponse.setSubscribes(List.of(subscribeDetailDto));

        return getSubscribeResponse;
    }

    // 구독 상품 추가
    public void AppendSubscribe(Long storeId, Long memberId, String memberRole,AppendSubscribeRequest requestDto) {
        if(memberRole.equals("BUYER")){
            throw new InvalidMemberRoleException();
        }
        // Store 정보 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        // Subscribe 엔티티 생성
        Subscribe subscribe = Subscribe.builder()
                .name(requestDto.getSubscribeName())
                .price(requestDto.getSubscribePrice())
                .description(requestDto.getSubscribeDescription())
                .rate(requestDto.getSubscribeRate())
                .store(store)
                .build();

        // 구독 상품 저장
        subscribeRepository.save(subscribe);

        // 주차별 구독 상품 설정
        List<SubscribeWeek> subscribeWeeks = requestDto.getSubscribeProducts().stream()
                .map(subscribeProductsDto -> {
                    //SubscribeWeek 엔티티 생성
                    SubscribeWeek subscribeWeek = SubscribeWeek.builder()
                            .date(LocalDate.parse(subscribeProductsDto.getSubscribeDate()))
                            .round(subscribeProductsDto.getSubscribeRound())
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now().plusWeeks(1))
                            .subscribe(subscribe)
                            .build();

                    // 주차별 반찬 설정
                    List<Food> foods = subscribeProductsDto.getSubscribeFood().stream()
                            .map(foodId ->
                                    foodRepository.findById((long) foodId)
                                            .orElseThrow(FoodNotFoundException::new)
                            ).collect(Collectors.toList());

                    //주차별 반찬 정보 설정
                    foods.forEach(food -> {
                        WeeklyFood weeklyFood = new WeeklyFood(food, subscribeWeek);
                        subscribeWeek.addWeeklyFood(weeklyFood);
                    });


                    return subscribeWeek;
                }).collect(Collectors.toList());

        //주차별 구독 상품 저장
        subscribeWeekRepository.saveAll(subscribeWeeks);
    }

    // 구독 상품 상세 보기
    @Transactional(readOnly = true)
    public SubscribeDetailResponse detailSubscribe(Long id) {
        //구독 상품 조회
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(SubscribeNotFoundException::new);

        // SubscribeDetailResponse 생성 및 기본 정보 설정
        SubscribeDetailResponse subscribeDetailResponse = new SubscribeDetailResponse();
        subscribeDetailResponse.setSubscribeId(subscribe.getId());
        subscribeDetailResponse.setSubscribeName(subscribe.getName());
        subscribeDetailResponse.setSubscribePrice(subscribe.getPrice());
        subscribeDetailResponse.setSubscribeDescription(subscribe.getDescription());
        subscribeDetailResponse.setSubscribeRate(subscribe.getRate());

        // 주차별 구독 정보 설정
        List<SubscribeDetailWeekDto> subscribeDetailWeeks = subscribe.getSubscribeWeeks().stream()
                .map(subscribeWeek -> {
                    SubscribeDetailWeekDto subscribeDetailWeekDto = new SubscribeDetailWeekDto();
                    subscribeDetailWeekDto.setSubscribeDate(subscribeWeek.getDate().toString());
                    subscribeDetailWeekDto.setSubscribeRound(subscribeWeek.getRound());

                    List<String> foodImgs = subscribeWeek.getWeeklyFoods().stream()
                            .map(weeklyFood -> weeklyFood.getFood().getImg())
                            .collect(Collectors.toList());
                    subscribeDetailWeekDto.setFoodImgs(foodImgs);

                    return subscribeDetailWeekDto;
                }).collect(Collectors.toList());

        subscribeDetailResponse.setSubscribeProducts(subscribeDetailWeeks);
        return subscribeDetailResponse;
    }
}
