package com.leaf.service.impl;

import com.leaf.domain.ShortUrl;
import com.leaf.mapper.ShortUrlMapper;
import com.leaf.service.ShortUrlService;
import com.leaf.utils.MathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.MurmurHash3;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShortUrlServiceImpl implements ShortUrlService {

    @Autowired
    private ShortUrlMapper shortUrlMapper;

    @Override
    public String generateShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("请输入正确url");
        }
        long hash = MurmurHash3.hash32x86(url.getBytes());
        return MathUtils._10_to_62(hash);
    }

    @Override
    public int saveShortUrl(ShortUrl shortUrl) {
        return shortUrlMapper.insert(shortUrl);
    }

    @Override
    public ShortUrl findByHashValueFromDB(String hashValue) {
        return shortUrlMapper.findByHashValue(hashValue);
    }

    @Override
    @Cacheable(cacheNames = "shortUrl", key= "#hashValue")
    public ShortUrl findByHashValueFromLocalCache(String hashValue) {
        log.info("==================从本地缓存中读取数据:[{}]==================", hashValue);
        return shortUrlMapper.findByHashValue(hashValue);
    }

    @Override
    public List<String> findAllHashValue() {
        return shortUrlMapper.findAllHashValue();
    }
}

