package com.blogspot.athletio;

import android.content.res.AssetManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import adapters.ChatBotMessageAdapter;
import general.ChatBotMessage;

/**
 * Created by zero639 on 9/30/17.
 */

/*
 Reference: https://medium.com/@harivigneshjayapalan/android-baking-a-simple-chatbot-in-30-minutes-aiml-ff43c3269025
 Causes: AIML, BotBrain and AIML parser
  */

public class ChatBotMain extends AppCompatActivity {


    private ListView chatListView;
    private FloatingActionButton chatButtonSend;
    private EditText chatEditTextMessage;
    private ImageView chatImageView;
    public Bot chatBotBrain;
    public static Chat chat;
    private ChatBotMessageAdapter chatBotAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_main);
        chatListView = (ListView) findViewById(R.id.chat_bot_listview);
        chatButtonSend = (FloatingActionButton) findViewById(R.id.chat_bot_btn_send_floatingbutton);
        chatEditTextMessage = (EditText) findViewById(R.id.chat_bot_edit_message_edittext);
        chatImageView = (ImageView) findViewById(R.id.chat_bot_image_imageview);
        chatBotAdapter = new ChatBotMessageAdapter(this, new ArrayList<ChatBotMessage>());
        chatListView.setAdapter(chatBotAdapter);

        chatButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ChatBotUserMessage = chatEditTextMessage.getText().toString();

                String ChatBotBrainMessage = chat.multisentenceRespond(chatEditTextMessage.getText().toString());
                if (TextUtils.isEmpty(ChatBotUserMessage)) {
                    return;
                }
                forwardMessage(ChatBotUserMessage);
                receiveBotMessage(ChatBotBrainMessage);
                chatEditTextMessage.setText("");
                chatListView.setSelection(chatBotAdapter.getCount() - 1);
            }
        });

        boolean a = isSdCardAvailable();
        AssetManager aimlAssets = getResources().getAssets();
        File aimlAssetsDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/mashrur/bots/Mashrur");
        boolean b = aimlAssetsDirectory.mkdirs();
        if (aimlAssetsDirectory.exists()) {
            try {
                for (String directoryParser : aimlAssets.list("Mashrur")) {
                    File SubDirectory = new File(aimlAssetsDirectory.getPath() + "/" + directoryParser);
                    boolean SubDirectoryCheck = SubDirectory.mkdirs();
                    for (String fileParser : aimlAssets.list("Mashrur/" + directoryParser)) {
                        File f = new File(aimlAssetsDirectory.getPath() + "/" + directoryParser + "/" + fileParser);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = aimlAssets.open("Mashrur/" + directoryParser + "/" + fileParser);
                        out = new FileOutputStream(aimlAssetsDirectory.getPath() + "/" + directoryParser + "/" + fileParser);
                        CopyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/mashrur";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
        chatBotBrain = new Bot("Mashrur", MagicStrings.root_path, "chat");
        chat = new Chat(chatBotBrain);
        String[] args = null;
        mainFunction(args);

    }

    private void forwardMessage(String MessageToForward) {
        ChatBotMessage chatBotUserMessage = new ChatBotMessage(MessageToForward, true, false);
        chatBotAdapter.add(chatBotUserMessage);

    }

    private void receiveBotMessage(String MessageToDisplay) {
        ChatBotMessage chatBotReply = new ChatBotMessage(MessageToDisplay, false, false);
        chatBotAdapter.add(chatBotReply);
    }

    private void forwardMessage() {
        ChatBotMessage chatMessage = new ChatBotMessage(null, true, true);
        chatBotAdapter.add(chatMessage);
        receiveBotMessage();
    }

    private void receiveBotMessage() {
        ChatBotMessage chatMessage = new ChatBotMessage(null, false, true);
        chatBotAdapter.add(chatMessage);
    }

    public static boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
    }

    private void CopyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void mainFunction(String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String userRequest = "Hello.";
        String botResponse = chat.multisentenceRespond(userRequest);
        System.out.println("Human: " + userRequest);
        System.out.println("Robot: " + botResponse);
    }

}
