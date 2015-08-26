package com.navid.trafalgar.mod.common;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.navid.gamemanager.rest.RestControl;
import com.navid.gamemanager.rest.RestGame;
import com.navid.gamemanager.rest.RestScope;
import com.navid.gamemanager.rest.impl.GameManagerClient;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.input.RemoteInputCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 8/5/15.
 */
public class SelectRemoteControlsScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;

    @Autowired
    private SelectRemoteControlsScreenController controller;

    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private AssetManager assetManager;

    @Override
    public void buildScreen(String screenUniqueId) {
        if (nifty.getScreen(screenUniqueId) != null) {
            nifty.removeScreen(screenUniqueId);
        }

    buildScreenNow(screenUniqueId);
}

    public void buildScreenNow(String screenUniqueId) {


        Collection<RemoteInputCommandStateListener> keyListeners
                = gameConfiguration.getPreGameModel().getByType(RemoteInputCommandStateListener.class);

        GameManagerClient gameManagerClient = new GameManagerClient("http://gamemanager.trafalgar.ws");

        RestGame game = gameManagerClient.createNewGame(RestScope.PUBLIC, "mode1", "map01");
        URL url = gameManagerClient.addPlayer(game, "helper", "helper", Lists.transform(newArrayList(keyListeners), new Function<RemoteInputCommandStateListener, RestControl>() {
            @Nullable
            @Override
            public RestControl apply(RemoteInputCommandStateListener input) {
                return new RestControl(input.getKey().toString(), "float", "group");

            }
        }));

        final File qrcode = generateQRCode(url);

        //sorting commands in alphabetical order of command name
        List<RemoteInputCommandStateListener> sortedCommands = newArrayList(keyListeners);
        Collections.sort(sortedCommands, new Comparator<RemoteInputCommandStateListener>() {
            @Override
            public int compare(RemoteInputCommandStateListener o1, RemoteInputCommandStateListener o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        final PanelBuilder outerPanelBuilder = new PanelBuilder("PartitionPanel") {
            {
                height("80%");
                childLayoutHorizontal();
                image(new ImageBuilder() {{
                    filename(qrcode.getName());
                }});
            }
        };

        final PopupBuilder popupBuilder = new PopupBuilder("popup") {
            {
                text("some text");
            }
        };

        List<List<RemoteInputCommandStateListener>> partitionedSorted = Lists.partition(sortedCommands, 4);

        int partitionIndex = 0;
        for (List<RemoteInputCommandStateListener> currentPartition : partitionedSorted) {

            final PanelBuilder partitionPanelBuilder = new PanelBuilder("Partition" + partitionIndex++ + "Panel") {
                {
                    height("80%");
                    childLayoutVertical();
                }
            };



            for (final RemoteInputCommandStateListener currentCommandListener : currentPartition) {
                final PanelBuilder commandNamePanelBuilder = new PanelBuilder(currentCommandListener.toString() + "Panel") {
                    {
                        childLayoutHorizontal();

                        text(new TextBuilder("text") {
                            {
                                text(currentCommandListener.toString());
                                style("nifty-label");
                                alignCenter();
                                valignCenter();
                                height("10%");
                                margin("1%");
                            }
                        });

                        control(new ListBoxBuilder(currentCommandListener.toString()) {
                            {
                                displayItems(4);
                                selectionModeSingle();
                                optionalHorizontalScrollbar();
                                optionalVerticalScrollbar();
                                alignCenter();
                                valignCenter();
                                height("10%");
                                width("10%");
                                margin("1%");
                            }
                        });

                    }
                };

                partitionPanelBuilder.panel(commandNamePanelBuilder);
            }

            outerPanelBuilder.panel(partitionPanelBuilder);
        }

        Screen screen = new ScreenBuilder(screenUniqueId) {
            {
                controller(controller); // Screen properties

                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        // <panel>
                        panel(outerPanelBuilder);
                        // </panel>
                        panel(new PanelBuilder("Panel_ID") {
                            {
                                height("20%");
                                childLayoutHorizontal();
                                control(new ButtonBuilder("PreviousButton", "Back") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("back()");
                                    }
                                });

                                control(new ButtonBuilder("NextButton", "Next") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("next()");
                                    }
                                });

                                control(new LabelBuilder("RepeatError") {
                                    {
                                        alignRight();
                                        valignCenter();
                                        text("");
                                        width("50%");
                                        color(new Color("#ff0"));
                                    }
                                });

                            }
                        });

                    }
                });
                // </layer>
            }
        }.build(nifty);
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

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    public void setController(SelectRemoteControlsScreenController controller) {
        this.controller = controller;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
