package com.example.notesapp.common;

import java.util.List;

public class ApiResponse {
    public List<?> items;
    public int page;
    public int size;
    public long total;

    public ApiResponse(List<?> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}
