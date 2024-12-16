package org.example.tile;

import org.example.GamePanel;
import org.example.utils.Mth;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    Tile[] tiles;
    int mapTileNum[][];

    public TileManager(GamePanel gp){
        this.gp = gp;
        tiles = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap();
    }
    public void getTileImage(){
        try{
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/textures/tiles/grass.png"));
            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/textures/tiles/water.png"));
            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/textures/tiles/brick.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(){
        try{
            InputStream is = getClass().getResourceAsStream("/maps/map01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col=0,row=0;
            while(row<gp.maxScreenRow){
                String line = br.readLine();
                while(col<gp.maxScreenCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col==gp.maxScreenCol){
                    col=0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            //e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        int col=0,row=0,x=0,y=0;

        while(col<gp.maxScreenCol&&row<gp.maxScreenRow){

            int tileNum = mapTileNum[col][row];
            g2.drawImage(tiles[tileNum].image,
            x + Mth.lerp(gp.prevActualX,gp.actualX,gp.lerpProgress),
            y + Mth.lerp(gp.prevActualY,gp.actualY,gp.lerpProgress)
            ,gp.tileSize,gp.tileSize,null);
            col++;
            x+=gp.tileSize;
            if(col==gp.maxScreenCol){
                col=0;
                x=0;
                row++;
                y+= gp.tileSize;
            }
        }
    }
}
