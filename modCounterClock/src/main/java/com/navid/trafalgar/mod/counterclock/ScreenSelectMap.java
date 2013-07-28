package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;

/**
 *
 * @author anf
 */
public class ScreenSelectMap implements ScreenController, BeanFactoryAware{

    private Nifty nifty;
    private Screen screen;
    private String selectedMap;
    private Boolean showGhost = Boolean.TRUE;
    private static final Logger logger = Logger.getLogger(ScreenSelectMap.class.getName());
    
    @Autowired
    private RecordPersistenceService persistence;
    
    @Autowired
    private GameConfiguration gameConfiguration;
    private XmlBeanFactory beanFactory;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        
        ListBox dropDown1 = screen.findNiftyControl("dropDown1", ListBox.class);
        dropDown1.addAllItems(getMaps());
        setSelectedMap((String) dropDown1.getSelection().get(0));
        
        
    }

    @Override
    public void onEndScreen() {
        ListBox dropDown1 = screen.findNiftyControl("dropDown1", ListBox.class);
        dropDown1.clear();
    }

    public void goTo(String nextScreen) {
        gameConfiguration.setMap(selectedMap);
        gameConfiguration.setShowGhost(showGhost);
        nifty.gotoScreen(nextScreen);  // switch to another screen
    }

    @NiftyEventSubscriber(id = "dropDown1")
    public void onMapSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        List<String> selection = event.getSelection();
        setSelectedMap(selection.get(0));
    }
    
    @NiftyEventSubscriber(id = "noghost")
    public void onNoGhost(final String id, final RadioButtonStateChangedEvent event) {
        showGhost = Boolean.FALSE;
    }

    @NiftyEventSubscriber(id = "besttime")
    public void onBestTime(final String id, final RadioButtonStateChangedEvent event) {
        showGhost = Boolean.TRUE;
    }

    private List<String> getMaps() {
        List<String> result = find("Games/Millestone2/", false);
        return result;
    }

    /**
     * This method returns the list of files under a given directory, working for both real folders
     * and jar content.
     * 
     * @param folder Folder
     * @param recursive Recursive?
     * @return List of strings with the path for the files (any).
     */
    protected List<String> find(String folder, boolean recursive) {

        List<String> result = new ArrayList<String>();

        URL url = this.getClass().getResource("/" + folder);
        
        File directory;
        try {
            directory = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidParameterException(folder);
        }

        if (directory.exists()) {
            addAllFilesInDirectory(directory, folder, recursive, result);
        } else {
            try {
                URLConnection urlConnection = url.openConnection();
                
                if (urlConnection instanceof JarURLConnection) {
                    JarURLConnection conn = (JarURLConnection) urlConnection;

                    JarFile jfile = conn.getJarFile();
                    Enumeration e = jfile.entries();
                    while (e.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) e.nextElement();
                        if(!entry.isDirectory() && entry.getName().contains(folder)){
                            result.add(entry.getName());
                        }
                    }
                }
            } catch (IOException e) {
                logger.logp(Level.SEVERE, this.getClass().toString(),
                        "find(pckgname, recursive, classes)", "Exception", e);
            } catch (Exception e) {
                logger.logp(Level.SEVERE, this.getClass().toString(),
                        "find(pckgname, recursive, classes)", "Exception", e);
            }
        }

        return result;
    }

    private void addAllFilesInDirectory(File directory, String folder, boolean recursive, List<String> result) {
        // Get the list of the files contained in the package
        File[] files = directory.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().contains(".json2");
            }
        });
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                if (files[i].isDirectory()) {
                    if (recursive) {
                        addAllFilesInDirectory(files[i],
                                folder + files[i].getName() + ".", true, result);
                    }
                } else {
                    result.add(folder + files[i].getName());
                }
            }
        }
    }

    private void setSelectedMap(String map) {

        List<CompetitorInfo> list = persistence.getTopCompetitors(4, map);

        ListBox dropDown1 = screen.findNiftyControl("listLocalTimes", ListBox.class);
        dropDown1.clear();
        for (CompetitorInfo currentTime : list) {
            dropDown1.addItem(currentTime.getTime());
        }

        selectedMap = map;

    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (XmlBeanFactory) beanFactory;
    }

    /**
     * @param persistence the persistence to set
     */
    public void setPersistence(RecordPersistenceService persistence) {
        this.persistence = persistence;
    }

}
