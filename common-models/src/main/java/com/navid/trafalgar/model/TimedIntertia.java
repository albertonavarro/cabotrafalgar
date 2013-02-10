package com.navid.trafalgar.model;

import java.util.LinkedList;

/**
 *
 * @author alberto
 */
public class TimedIntertia {
    
    private float maxTime = 5;
    private float sumTime = 0;
    private float sumSpeed = 0;
    
    private static class Pair{
        final public float time;
        
        final public float speed;
        
        public Pair( final float value1, final float value2){
            this.time = value1;
            this.speed = value2;
        }
    }
    
    private LinkedList<Pair> queue = new LinkedList<Pair>();
    
    public float getAverage(float time, float speed){
        
        queue.add(new Pair(time, speed));
        
        sumTime += time;
        sumSpeed += speed;
        
        while (sumTime > maxTime){
            Pair pair = queue.remove();
            sumTime -= pair.time;
            sumSpeed -= pair.speed;
        }
        
        return ( ( sumTime / maxTime * sumSpeed / queue.size())  );
        
    }
    
}
