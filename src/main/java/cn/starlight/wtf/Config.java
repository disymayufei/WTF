package cn.starlight.wtf;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final File confDir = FabricLoader.getInstance().getConfigDir().toFile();
    private final File dir = new File(confDir, "WTF");
    private final File conf_file = new File(dir, "config.yml");

    private final Map<String, Object> default_conf = new HashMap<>();
    private Map<String, Object> conf;

    public Config(){
        default_conf.put("enable", true);
        default_conf.put("max_distance", 256.0);
        default_conf.put("debug", false);

        if (!conf_file.exists()) {
            saveDefaultConfig();
        }
        this.conf = readConfig();
    }

    public boolean isDebug(){
        if(conf == null){
            return false;
        }
        else {
            Object val = conf.getOrDefault("debug", false);
            if(val instanceof Boolean){
                return (boolean)val;
            }
            else {
                return false;
            }
        }
    }

    public boolean isEnable(){
        if(conf == null){
            return false;
        }
        else {
            Object val = conf.getOrDefault("enable", false);
            if(val instanceof Boolean){
                return (boolean)val;
            }
            else {
                return false;
            }
        }
    }

    public void setEnable(boolean enable){
        conf.put("enable", enable);
        saveConfig();
    }

    public double getMaxDistance(){
        if(conf == null){
            return 256.0;
        }
        else {
            Object val = conf.getOrDefault("max_distance", 256.0);
            if(val instanceof Number){
                if(val instanceof Long){
                    return 256.0;
                }
                else {
                    double final_val = (double)val;
                    if(final_val <= 0){
                        return 256.0;
                    }
                    else {
                        return final_val;
                    }
                }
            }
            else {
                return 256.0;
            }
        }
    }

    public void setMaxDistance(double distance){
        conf.put("max_distance", distance);
        saveConfig();
    }

    public void reloadConfig(){
        this.conf = readConfig();
    }

    public void saveDefaultConfig(){  // 不存在config时创建默认设置
        dir.mkdirs();

        if (!conf_file.exists()) {
            try {
                conf_file.createNewFile();
            } catch (IOException e) {
                Logger.error("配置文件生成失败，请重启服务器，以下是错误的堆栈信息：");
                e.printStackTrace();
            }
        }

        Yaml default_yaml = new Yaml();
        String config = default_yaml.dumpAsMap(default_conf);

        try {
            Files.writeString(conf_file.toPath(), config);
        } catch (IOException e) {
            Logger.error("配置文件写入失败，请重启服务器，以下是错误的堆栈信息：");
            e.printStackTrace();
        }
    }

    private Map<String, Object> readConfig(){

        if(!conf_file.exists()){
            this.saveDefaultConfig();
            return default_conf;
        }

        Yaml yaml = new Yaml();
        Map<String, Object> config = null;
        try {
            config = yaml.load(Files.readString(conf_file.toPath()));

            if(config == null){
                this.saveDefaultConfig();
                return default_conf;
            }
        } catch (IOException e) {
            Logger.error("配置文件读取失败，请重启服务器，以下是错误的堆栈信息：");
            e.printStackTrace();
        }

        return config;
    }

    private void saveConfig(){
        Yaml default_yaml = new Yaml();
        String config = default_yaml.dumpAsMap(conf);

        try {
            Files.writeString(conf_file.toPath(), config);
        } catch (IOException e) {
            Logger.error("配置文件写入失败，请重启服务器，以下是错误的堆栈信息：");
            e.printStackTrace();
        }
    }
}
