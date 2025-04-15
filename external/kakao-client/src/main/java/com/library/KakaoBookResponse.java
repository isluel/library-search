package com.library;

import java.util.List;

// record 로 만들어봄..!!
public record KakaoBookResponse(
        List<Documents> documents,
        Meta meta
) {

}
