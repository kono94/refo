package evironment.jumpingDino;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;


@AllArgsConstructor
@Getter
public abstract class RenderObject {
    protected int size;
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected Color color;

    public void render(Graphics g){
        g.setColor(color);
        g.fillRect(x, y, size, size);
    }

    public void tick(){
        y += dy;
        x += dx;
    }
}
