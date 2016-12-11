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
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.LabelControl;
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
public class SelectRemoteControlsScreenController extends GameMenuController {

    @Autowired
    private GameConfiguration gameConfiguration;

    @Override
    public void doOnStartScreen() {

        final Collection<RemoteInputCommandStateListener> remoteListeners
                = gameConfiguration.getPreGameModel().getByType(RemoteInputCommandStateListener.class);

        if(remoteListeners.isEmpty()){
            screenFlowManager.setNextScreenHint(ScreenFlowManager.SKIP);
            nifty.gotoScreen("redirector");
            return;
        }

        try{
            final GameManagerClient gameManagerClient = new GameManagerClient("http://gamemanager.trafalgar.ws:8080");

            final RestGame game = new HystrixCommand<RestGame>(HystrixCommandGroupKey.Factory.asKey("Gameserver"), 5000) {
                @Override
                public final RestGame run() throws Exception {
                    return gameManagerClient.createNewGame(RestScope.PUBLIC, "mode1", "map01");
                }
            }.execute();

            URL url = new HystrixCommand<URL>(HystrixCommandGroupKey.Factory.asKey("Gameserver"), 5000) {
                @Override
                public final URL run() throws Exception {
                    return gameManagerClient.addPlayer(game, "helper", "helper", Lists.transform(newArrayList(remoteListeners), new Function<RemoteInputCommandStateListener, RestControl>() {
                        @Nullable
                        @Override
                        public RestControl apply(RemoteInputCommandStateListener input) {
                            return new RestControl(input.getKey().toString(), "float", "group");
                        }
                    }));
                }
            }.execute();


            final File qrcode = generateQRCode(url);

            enrichWithGameAndUser(remoteListeners, game.getId(), "user");

            NiftyImage newImage = nifty.createImage(qrcode.getName(), false);
            Element qrCodeImage = screen.findElementByName("qrCodeImage");
            qrCodeImage.getRenderer(ImageRenderer.class).setImage(newImage);


        }catch (Exception e) {
            Label label = screen.findNiftyControl("errorMessage", Label.class);
            label.setText("Error accessing gameserver. Please select other controler or try later");
        }

    }

    @Override
    public void onEndScreen() {

    }

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

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }
}
