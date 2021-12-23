package com.mm.kim.mentormentee.common.util.pagination;

import lombok.Getter;

@Getter
public class Paging {

    private String url;
    private int curPage;    //현재페이지 : 외부에서 받아와야함
    private int blockCnt;    //페이지 블록당 페이지 개수
    private int total;        //전체 게시물 수
    private int cntPerPage; //페이지 당 뿌릴 게시물 수

    private int lastPage;    //마지막 페이지
    private int prev;        //이전 페이지 값
    private int next;        //다음 페이지 값
    private int blockStart;    //블록 시작 값
    private int blockEnd;    //블록 끝 값

    //빌더패턴으로 만들자
    public Paging(Builder builder) {
        this.url = builder.url;
        this.curPage = builder.curPage;
        this.blockCnt = builder.blockCnt;
        this.total = builder.total;
        this.cntPerPage = builder.cntPerPage;
        this.lastPage = (int) Math.ceil((double) total / cntPerPage);
        this.prev = curPage == 1 ? 1 : curPage - 1;
        this.next = curPage == lastPage ? lastPage : curPage + 1;
        calBlockStartAndEnd();
    }

    public void calBlockStartAndEnd() {
        this.blockStart = (curPage - 1) / blockCnt * blockCnt + 1;
        int end = blockStart + blockCnt - 1;
        this.blockEnd = end > lastPage ? lastPage : end;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int curPage;    //현재페이지 : 외부에서 받아와야함
        private int blockCnt;    //페이지 블록당 페이지 개수
        private int total;        //전체 페이지 수(=마지막 페이지)
        private String url;
        private int cntPerPage;

        public Builder curPage(int curPage) {
            this.curPage = curPage;
            return this;
        }

        public Builder blockCnt(int blockCnt) {
            this.blockCnt = blockCnt;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder cntPerPage(int cntPerPage) {
            this.cntPerPage = cntPerPage;
            return this;
        }

        public Paging build() {
            return new Paging(this);
        }
    }
}
