package com.reclizer.csgobox;

import com.mojang.logging.LogUtils;
import com.reclizer.csgobox.gui.RecModScreens;
import com.reclizer.csgobox.sounds.ModSounds;
import com.reclizer.csgobox.config.CsgoBoxManage;
import com.reclizer.csgobox.gui.RecModMenus;
import com.reclizer.csgobox.item.ModItems;
import com.reclizer.csgobox.packet.Networking;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CsgoBox.MODID)
public class CsgoBox {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "csgobox";
    // Directly reference a slf4j logger
    //public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "csgobox" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "csgobox" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "csgobox:example_block", combining the namespace and path

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab


    public CsgoBox() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading

        modEventBus.addListener(this::commonSetup);
        ModSounds.SOUNDS.register(modEventBus);
        RecModMenus.register(modEventBus);
        ModItems.register(modEventBus);
        ModItems.registerTab(modEventBus);



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        Path configPath = FMLPaths.CONFIGDIR.get(); // 获取Minecraft配置目录
        Path folderPath = configPath.resolve("csbox");
        try {
            // 创建文件夹（如果不存在）
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            String content =
                    """
                    {
                      "name": "Weapons Supply Box",
                      "key": "csgobox:csgo_key0",
                      "drop": 0.12,
                      "random": [
                        2,
                        5,
                        6,
                        20,
                        625
                      ],
                      "entity": [
                        "minecraft:zombie",
                        "minecraft:skeleton"
                      ],
                      "grade1": [
                        "{\\"id\\":\\"minecraft:stone_sword\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:iron_axe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:iron_shovel\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:iron_pickaxe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:iron_axe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:iron_hoe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:iron_sword\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}"
                      ],
                      "grade2": [
                        "{\\"id\\":\\"minecraft:golden_sword\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:golden_axe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:golden_axe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:golden_pickaxe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:golden_shovel\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}"
                      ],
                      "grade3": [
                        "{\\"id\\":\\"minecraft:diamond_shovel\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:diamond_pickaxe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:diamond_hoe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}"
                      ],
                      "grade4": [
                        "{\\"id\\":\\"minecraft:diamond_axe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:diamond_sword\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}"
                      ],
                      "grade5": [
                        "{\\"id\\":\\"minecraft:netherite_sword\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:netherite_axe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:netherite_pickaxe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:netherite_shovel\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}",
                        "{\\"id\\":\\"minecraft:netherite_hoe\\",\\"Count\\":1,\\"tag\\":{\\"Damage\\":0}}"
                      ]
                    }""";

            if (!Files.exists(folderPath.resolve("default.json"))) {
                Path filePath = folderPath.resolve("default.json");
                // 创建文件并写入内容new FileWriter(filePath.toFile())
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath.toFile()), StandardCharsets.UTF_8))) {
                    writer.write(content);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        event.enqueueWork(() -> {
            try {
                CsgoBoxManage.loadConfigBox();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Some common setup code
        Networking.registerMessages();
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

//        if (Config.logDirtBlock)
//            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
//
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {


            event.enqueueWork(RecModScreens::clientLoad);

            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
