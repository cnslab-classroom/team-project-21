package org.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed;

    private boolean[] numberKeys = new boolean[10];      // 현재 키가 눌린 상태
    private boolean[] keyJustPressed = new boolean[10];  // 눌렀다 뗀 상태(한 번만 실행)

    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
            default -> {
                // 숫자 키 (1~9) 처리
                if (code >= KeyEvent.VK_1 && code <= KeyEvent.VK_9) {
                    int keyIndex = code - KeyEvent.VK_0; // 숫자 키 인덱스 (1~9)
                    if (!numberKeys[keyIndex]) { // 처음 눌렀을 때만 처리
                        numberKeys[keyIndex] = true;
                        keyJustPressed[keyIndex] = true;
                    }
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_SPACE -> spacePressed = false;
            default -> {
                // 숫자 키 (1~9) 처리
                if (code >= KeyEvent.VK_1 && code <= KeyEvent.VK_9) {
                    int keyIndex = code - KeyEvent.VK_0; // 숫자 키 인덱스 (1~9)
                    numberKeys[keyIndex] = false; // 키를 떼면 상태 초기화
                }
            }
        }
    }

    /**
    * 숫자 키(1~9)가 눌렀다 떼어진 이벤트를 한 번만 감지
    * @param keyIndex 숫자 키 (1~9)
    * @return true면 한 번 실행됨
    */
    public boolean isNumberKeyJustPressed(int keyIndex) {
        if (keyIndex < 1 || keyIndex > 9) return false; // 범위 초과 방지
        if (keyJustPressed[keyIndex]) {
            keyJustPressed[keyIndex] = false; // 처리 후 초기화
            return true;
        }
        return false;
    }
}
