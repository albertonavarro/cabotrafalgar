package com.navid.trafalgar.model;

import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.definition2.GameDefinition2;
import java.util.*;

/**
 *
 * @author alberto
 */
public class Builder2 {
    
    public enum Category  { ship, item, context, other}
    public enum Interactivity { none, player, ghost}
    
    Map<Category, Collection<BuilderInterface>> buildersByCategory = new EnumMap<Category, Collection<BuilderInterface>>(Category.class);
    Map<String, BuilderInterface> buildersByName = new HashMap<String, BuilderInterface>();
    
    public Builder2(){
        buildersByCategory.put(Category.ship, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.context, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.item, new LinkedList<BuilderInterface>());
        buildersByCategory.put(Category.other, new LinkedList<BuilderInterface>());
    }
    
    public GameModel build( GameConfiguration gameConfiguration, GameDefinition2 gameDef){
        GameModel gameModel = new GameModel();
        
        for ( Entry entry : gameDef.getEntries()){
            if(entry.getName() != null && entry.getName().equals("player")){
                entry.setType(gameConfiguration.getShipName());
            }
            
            Object o = build(entry);
            gameModel.addToModel(o);
        }
        
        for (Dependent currentDependant : (List<Dependent>) gameModel.getByType(Dependent.class)){
            currentDependant.resolveDependencies(gameModel);
        }

        //Wind
      /*  SimpleContext simpleContext = new SimpleContext();
        gameModel.setiContext(simpleContext);
        if (gameDef.getWind() != null) {
            RotateSimpleWind wind = (RotateSimpleWind) build("Context");
            wind.setWind(gameDef.getWind().getVector2f());
            simpleContext.setWind(wind);
        }
        
        for (java.util.Map.Entry<String, ShipDefinition> shipDefEntry : gameDef.getShips().entrySet()) {
            AShipModel shipModel = (AShipModel) build(gameConfiguration.getShipName());
            gameModel.getShips().put(shipDefEntry.getKey(), shipModel);
            shipModel.setContext(simpleContext);
            shipModel.setWindNode(gameModel.getiContext().getWind().createWindGeometryNode());
            
            gameModel.getGameNode().attachChild(shipModel);
        }
        
        //Sun
        if (gameDef.getSun() != null) {
            SunModel sunModel = new SunModel(gameDef.getSun().getVector3f(), gameDef.getSun().getColorRGBA());
            gameModel.setSun(sunModel);
            gameNode.addLight(sunModel);
        }
        //Extra light;
        gameNode.addLight(new AmbientLight());

       */ 
        
        return gameModel;
    }
    
    
    public void registerBuilder(BuilderInterface builder){
        
        for(Category currentCategory : builder.getCategories()){
            buildersByCategory.get(currentCategory).add(builder);
        }
        
        if(buildersByName.get(builder.getType()) != null ){
            throw new IllegalStateException("Builder " + builder.getType() + " already exists");
        }
        
        buildersByName.put(builder.getType(), builder);
    }
    
    public Collection<BuilderInterface> getBuilder(Category category){
        return buildersByCategory.get(category);
    }
    
    public Object build(Entry entry){
        if(buildersByName.get(entry.getType()) == null){
            throw new IllegalArgumentException("Builder " + entry.getType() + " doesn't exist");
        }
        
        return buildersByName.get(entry.getType()).build(entry.getName(), entry.getValues());
    }
    
    
    
}
