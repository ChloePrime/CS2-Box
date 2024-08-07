package com.reclizer.csgobox.config;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.reclizer.csgobox.CsgoBox;
import com.reclizer.csgobox.item.ItemCsgoBox;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public class CsgoBoxManage {

    private static final Gson GSON = new Gson();
    private static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.relative().resolve("csbox");

    public static final List<ItemCsgoBox.BoxInfo> BOX = Lists.newArrayList();

    public static void loadConfigBox() throws IOException {
        if (!Files.isDirectory(CONFIG_DIR)) {
            Files.createDirectories(CONFIG_DIR);
        }

        try (var stream = Files.walk(CONFIG_DIR, 1)) {
            boolean[] init = {false};
            stream.skip(1).filter(path -> path.getFileName().toString().endsWith(".json")).forEach(boxConfig -> {
                if (!init[0]) {
                    init[0] = true;
                    BOX.clear();
                }
                try {
                    BOX.add(GSON.fromJson(Files.newBufferedReader(boxConfig), ItemCsgoBox.BoxInfo.class));
                } catch (Exception ex) {
                    CsgoBox.LOGGER.error("Failed reading %s".formatted(boxConfig.getFileName().toString()), ex);
                }
            });
        }
    }

    public static void updateBoxJson(String name, List<String> item, List<Integer> grade) throws IOException {
        //"random": [2, 5, 6,20, 625],
        // 创建新的 JSON 对象
        JsonObject newObject = new JsonObject();
        newObject.addProperty("name", name);
        newObject.addProperty("key", "csgobox:csgo_key0");
        newObject.addProperty("drop", 0);

        JsonArray jsonInt = new JsonArray();
        jsonInt.add(2);
        jsonInt.add(5);
        jsonInt.add(6);
        jsonInt.add(20);
        jsonInt.add(625);
        newObject.add("random", jsonInt);

        JsonArray jsonArray0 = new JsonArray();
        jsonArray0.add("minecraft:zombie");
        newObject.add("entity", jsonArray0);

        JsonArray jsonArray1 = new JsonArray();
        JsonArray jsonArray2 = new JsonArray();
        JsonArray jsonArray3 = new JsonArray();
        JsonArray jsonArray4 = new JsonArray();
        JsonArray jsonArray5 = new JsonArray();

        if (!item.isEmpty()) {
            for (int i = 0; i < item.size(); i++) {
                switch (grade.get(i)) {
                    case 1 -> jsonArray1.add(item.get(i));
                    case 2 -> jsonArray2.add(item.get(i));
                    case 3 -> jsonArray3.add(item.get(i));
                    case 4 -> jsonArray4.add(item.get(i));
                    case 5 -> jsonArray5.add(item.get(i));
                }
            }
        }


        newObject.add("grade1", jsonArray1);
        newObject.add("grade2", jsonArray2);
        newObject.add("grade3", jsonArray3);
        newObject.add("grade4", jsonArray4);
        newObject.add("grade5", jsonArray5);


        // 将更新后的数组写回文件
        writeJsonFile(CONFIG_DIR.resolve(name + ".json"), newObject);
    }

    private static void writeJsonFile(Path filePath, JsonElement jsonElement) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, jsonElement.toString(), StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
        } catch (IOException ex) {
            CsgoBox.LOGGER.error("Failed to save box config", ex);
        }
    }
}
