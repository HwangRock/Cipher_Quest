package com.example.cipherquest.service;

import com.example.cipherquest.persistence.StageRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.*;

@Slf4j
@Service
public class StageService {
    @Autowired
    private StageRepository stageRepository;

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public StageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //redis create, update with TTL
    public void setToRedisWithTTL(String key, Object value, long durationInSeconds){
        try{
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(durationInSeconds));
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Failed to set value to redis");
        }
    }

    //redis read
    public Object getFromRedis(String key){
        try{
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Failed to retrieve value from redis");
        }
    }

    //redis delete
    public void deleteFromRedis(String key){
        try{
            redisTemplate.delete(key);
        }catch(Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Failed to delete value from redis");
        }
    }

    //check TTL. 음수 양수로 유효성을 확인
    public Long getTTL(String key){
        return redisTemplate.getExpire(key);
    }

    public Boolean checkKey(String key){
        Pattern pattern=Pattern.compile("^[a-z0-9]+$");
        Matcher matcher=pattern.matcher(key);
        return matcher.matches();
    }

    // 위키피디아에서 랜덤으로 올바른 페이지를 찾을때까지 찾고 크롤링.
    public String[] crawling() throws IOException {
        String str="";
        String sourceUrl = "";
        while(true) {
            try{
                String url = "https://en.wikipedia.org/wiki/Special:Random";
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .get();

                sourceUrl = doc.location();
                Elements paragraphs = doc.select("div#mw-content-text > div.mw-parser-output > p");

                List<String> sentences = new ArrayList<>();

                for (Element p : paragraphs) {
                    String text = p.text().trim();

                    text = text.replaceAll("\\[\\d+\\]", ""); //[]를 제거해서 올바르게 파싱

                    int txtLen = text.length();
                    if (txtLen < 30 || txtLen > 250) { // 적당한 길이
                        continue;
                    }

                    String[] splitSentences = text.split("\\. ");
                    for (String s : splitSentences) {
                        s = s.trim();
                        if (s.length() > 20) {
                            if (!s.matches("^[a-zA-Z0-9 ,.'\"\\-()\\[\\]?!:;]+$")){
                                continue;
                            }

                            if(s.endsWith(".")){
                                sentences.add(s);
                            }
                            else{
                                sentences.add(s+".");
                            }
                        }
                    }
                }


                Random rand = new Random();
                if(sentences.size()!=0){
                    str = sentences.get(rand.nextInt(sentences.size()));
                    break;
                }
            }catch(HttpStatusException e){
                log.warn("404 error: dont find page. try again.."); // 페이지 못찾았으면 다시 크롤링
                continue;
            }
        }
        return new String[]{str,sourceUrl};
    }
}
