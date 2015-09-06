package com.navid.trafalgar.mod.common;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.navid.gamemanager.rest.RestControl;
import com.navid.gamemanager.rest.RestGame;
import com.navid.gamemanager.rest.RestScope;
import com.navid.gamemanager.rest.impl.GameManagerClient;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.RemoteInputCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Hashtable;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 8/5/15.
 */
public class SelectRemoteControlsScreenController implements ScreenController {

    /**
     * From bind
     */
    private Nifty nifty;
    /**
     * From bind
     */
    private Screen screen;


    @Autowired
    private GameConfiguration gameConfiguration;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        Collection<RemoteInputCommandStateListener> remoteListeners
                = gameConfiguration.getPreGameModel().getByType(RemoteInputCommandStateListener.class);

        if(remoteListeners.isEmpty()){
            screenFlowManager.setNextScreenHint(ScreenFlowManager.SKIP);
            nifty.gotoScreen("redirector");
        }

        GameManagerClient gameManagerClient = new GameManagerClient("http://gamemanager.trafalgar.ws");

        RestGame game = gameManagerClient.createNewGame(RestScope.PUBLIC, "mode1", "map01");
        URL url = gameManagerClient.addPlayer(game, "helper", "helper", Lists.transform(newArrayList(remoteListeners), new Function<RemoteInputCommandStateListener, RestControl>() {
            @Nullable
            @Override
            public RestControl apply(RemoteInputCommandStateListener input) {
                return new RestControl(input.getKey().toString(), "float", "group");

            }
        }));

        final File qrcode = generateQRCode(url);

        enrichWithGameAndUser(remoteListeners, game.getId(), "user");

        NiftyImage newImage = nifty.createImage(qrcode.getName(), false);
        Element qrCodeImage = screen.findElementByName("qrCodeImage");
        qrCodeImage.getRenderer(ImageRenderer.class).setImage(newImage);
    }

    @Override
    public void onEndScreen() {

    }

    @Autowired
    private ScreenFlowManager screenFlowManager;

    private void enrichWithGameAndUser(Collection<RemoteInputCommandStateListener> keyListeners, Long id, String user) {
        for(RemoteInputCommandStateListener current : keyListeners) {
            current.setGameId(id);
            current.setUserId(user);
        }
    }

    private File generateQRCode(URL url)  {

        try {
            File tmp = File.createTempFile("qrcode", ".jpg");
            int size = 125;
            String fileType = "jpg";
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(url.toString(), BarcodeFormat.QR_CODE, size, size, hintMap);
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(java.awt.Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(java.awt.Color.BLACK);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, tmp);
            return tmp;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void next() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.NEXT);
        nifty.gotoScreen("redirector");
    }

    public void back() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.PREV);
        nifty.gotoScreen("redirector");
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }
}