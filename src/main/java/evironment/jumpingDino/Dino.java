package evironment.jumpingDino;

import lombok.Getter;

import java.awt.*;

public class Dino extends RenderObject {
    @Getter
    private boolean inJump;

    public Dino(int size, int x, int y, int dx, int dy, Color color) {
        super(size, x, y, dx, dy, color);
    }

    public void jump(){
        if(!inJump){
            dy = -Config.DINO_JUMP_SPEED;
            inJump = true;
        }
    }

    private void fall(){
        if(inJump){
            dy = Config.DINO_JUMP_SPEED;
        }
    }

    @Override
    public void tick(){
        // reached max jump height
        int topOfDino = Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE;

        if(y + dy <= topOfDino - Config.MAX_JUMP_HEIGHT) {
            fall();
        } else if(y + dy >= topOfDino) {
            inJump = false;
            dy = 0;
            y = Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE;
        }
        super.tick();
    }
}
