package org.example.entity;

public class HitBox {
    private float x, y, z; // 중심의 x, y, z 좌표
    private float width, height, depth; // 히트박스의 너비, 높이, 깊이 (xz 평면과 y축 기반)

    public HitBox(float x, float y, float z, float width, float height, float depth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    // Getter and Setter
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getZ() { return z; }
    public void setZ(float z) { this.z = z; }

    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getDepth() { return depth; }
    public void setDepth(float depth) { this.depth = depth; }

    // 히트박스 경계 계산
    public float getMinX() { return x - width / 2; }
    public float getMaxX() { return x + width / 2; }

    public float getMinY() { return y; }
    public float getMaxY() { return y + height; }

    public float getMinZ() { return z - depth / 2; }
    public float getMaxZ() { return z + depth / 2; }

    // 히트박스 이동
    public void move(float dx, float dy, float dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    // 충돌 감지: 다른 히트박스와의 충돌 여부 확인
    public boolean intersects(HitBox other) {
        return !(this.getMaxX() < other.getMinX() || this.getMinX() > other.getMaxX() ||
                 this.getMaxY() < other.getMinY() || this.getMinY() > other.getMaxY() ||
                 this.getMaxZ() < other.getMinZ() || this.getMinZ() > other.getMaxZ());
    }

    // 히트박스 포함 여부: 특정 점이 히트박스 내에 있는지 확인
    public boolean contains(float px, float py, float pz) {
        return (px >= getMinX() && px <= getMaxX() &&
                py >= getMinY() && py <= getMaxY() &&
                pz >= getMinZ() && pz <= getMaxZ());
    }

    @Override
    public String toString() {
        return String.format("Hitbox3D[x=%.2f, y=%.2f, z=%.2f, width=%.2f, height=%.2f, depth=%.2f]", 
                              x, y, z, width, height, depth);
    }
}
