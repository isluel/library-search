package com.library;

import java.util.List;

// record 로 만들어봄..!!
public record Documents (
        String title,
        List<String> authors,
        String isbn,
        String publisher,
        String datetime
) {

}
