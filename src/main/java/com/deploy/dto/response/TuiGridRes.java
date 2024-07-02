package com.deploy.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Toast UI Grid 응답객체
 */
@Getter
public class TuiGridRes {

    private boolean result;
    private Data data;

    public TuiGridRes(List<?> contents, int page, int totalCount, int perPage) {

        this.result = true;
        this.data = Data.builder()
                .contents(contents)
                .pagination(
                        Data.Pagination.builder()
                                .page(page)
                                .totalCount(totalCount)
                                .perPage(perPage)
                                .build()
                )
                .build();
    }

    @Getter
    @Builder
    public static class Data {

        private List<?> contents;
        private Pagination pagination;


        @Getter
        @Builder
        public static class Pagination {

            private int page;
            private int totalCount;
            private int perPage;

        }

    }
}
