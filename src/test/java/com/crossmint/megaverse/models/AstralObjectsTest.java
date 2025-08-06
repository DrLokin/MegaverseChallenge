package com.crossmint.megaverse.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AstralObjectsTest {

    AstralObject obj1,obj2,obj3,obj4;

    @BeforeEach
    public void setUp(){
        obj1 = AstralObject.createAstralObject("SPACE",3,6);
        obj2 = AstralObject.createAstralObject("POLYANET",4,1);
        obj3 = AstralObject.createAstralObject("BLUE_SOLOON",5,9);
        obj4 = AstralObject.createAstralObject("LEFT_COMETH",7,7);
    }

    @AfterEach
    public void tearDown(){
        obj1 = null;
        obj2 = null;
        obj3 = null;
        obj4 = null;
    }


    @Test
    public void testCreateAstralObject(){

        Assertions.assertNull(obj1);
        Assertions.assertInstanceOf(Polyanet.class,obj2);
        Assertions.assertInstanceOf(Soloon.class,obj3);
        Assertions.assertInstanceOf(Cometh.class,obj4);

        Assertions.assertEquals(4,obj2.getRow());
        Assertions.assertEquals(5,obj3.getRow());
        Assertions.assertEquals(7,obj4.getRow());

        Assertions.assertEquals(1,obj2.getCol());
        Assertions.assertEquals(9,obj3.getCol());
        Assertions.assertEquals(7,obj4.getCol());

    }


    @Test
    public void testPolyanets(){
        Assertions.assertEquals(AstralObject.AstralName.POLYANET,obj2.getAstralName());
    }

    @Test
    public void testSoloons(){
        Assertions.assertEquals(AstralObject.AstralName.SOLOON,obj3.getAstralName());

        Soloon moon = (Soloon) obj3;
        Assertions.assertEquals(Soloon.MoonColors.BLUE,moon.getColor());

        moon = (Soloon) AstralObject.createAstralObject("RED_SOLOON",5,9);
        Assertions.assertNotNull(moon);
        Assertions.assertEquals(Soloon.MoonColors.RED,moon.getColor());

        moon = (Soloon) AstralObject.createAstralObject("PURPLE_SOLOON",5,9);
        Assertions.assertNotNull(moon);
        Assertions.assertEquals(Soloon.MoonColors.PURPLE,moon.getColor());

        moon = (Soloon) AstralObject.createAstralObject("WHITE_SOLOON",5,9);
        Assertions.assertNotNull(moon);
        Assertions.assertEquals(Soloon.MoonColors.WHITE,moon.getColor());

    }

    @Test
    public void testComeths(){
        Assertions.assertEquals(AstralObject.AstralName.COMETH,obj4.getAstralName());

        Cometh comet = (Cometh) obj4;
        Assertions.assertEquals(Cometh.ComethDirs.LEFT,comet.getDirection());

        comet = (Cometh) AstralObject.createAstralObject("RIGHT_COMETH",5,9);
        Assertions.assertNotNull(comet);
        Assertions.assertEquals(Cometh.ComethDirs.RIGHT,comet.getDirection());

        comet = (Cometh) AstralObject.createAstralObject("UP_COMETH",5,9);
        Assertions.assertNotNull(comet);
        Assertions.assertEquals(Cometh.ComethDirs.UP,comet.getDirection());

        comet = (Cometh) AstralObject.createAstralObject("DOWN_COMETH",5,9);
        Assertions.assertNotNull(comet);
        Assertions.assertEquals(Cometh.ComethDirs.DOWN,comet.getDirection());

    }



}
