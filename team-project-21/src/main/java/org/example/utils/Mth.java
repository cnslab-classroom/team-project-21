package org.example.utils;

//MathHelper의 줄임말. 선형 보간 함수, 기타 복잡한 함수들 보관하는 용도
public class Mth {
    public static int lerp(int start, int end, double t) {
        return (int) (start + t * (end - start));
    }    
}
