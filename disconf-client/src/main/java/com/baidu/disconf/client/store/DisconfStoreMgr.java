package com.baidu.disconf.client.store;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.inter.IDisconfUpdate;
import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterFile.FileItemValue;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.store.inner.DisconfCenterStore;
import com.baidu.utils.ClassUtils;

/**
 * 仓库模块
 * 
 * @author liaoqiqi
 * @version 2014-6-6
 */
public class DisconfStoreMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfStoreMgr.class);

    private DisconfStoreMgr() {

    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static DisconfStoreMgr instance = new DisconfStoreMgr();
    }

    public static DisconfStoreMgr getInstance() {
        return SingletonHolder.instance;
    }

    // 核心仓库
    private DisconfCenterStore disconfCenterStore = new DisconfCenterStore();

    /**
     * 批量添加配置文件
     * 
     * @return
     */
    public void transformScanFiles(List<DisconfCenterFile> disconfCenterFiles) {

        for (DisconfCenterFile disconfCenterFile : disconfCenterFiles) {
            disconfCenterStore.storeOneFile(disconfCenterFile);
        }
    }

    /**
     * 获取配置文件的数据
     * 
     * @return
     */
    public Map<String, DisconfCenterFile> getConfFileMap() {
        return disconfCenterStore.getConfFileMap();
    }

    /**
     * 批量添加配置项
     * 
     * @return
     */
    public void transformScanItems(List<DisconfCenterItem> disconfCenterItems) {

        for (DisconfCenterItem disconfCenterItem : disconfCenterItems) {
            disconfCenterStore.storeOneItem(disconfCenterItem);
        }
    }

    /**
     * 获取配置项的数据
     * 
     * @return
     */
    public Map<String, DisconfCenterItem> getConfItemMap() {
        return disconfCenterStore.getConfItemMap();
    }

    /**
     * 是否有这个配置
     * 
     * @return
     */
    public boolean hasThisConf(String key) {

        // 配置文件
        if (disconfCenterStore.getConfFileMap().containsKey(key)) {
            return true;
        }

        // 配置项
        if (disconfCenterStore.getConfItemMap().containsKey(key)) {
            return true;
        }

        return false;
    }

    /**
     * 添加一个更新 的回调函数
     */
    public void addUpdateCallback(String key, IDisconfUpdate iDisconfUpdate) {

        if (disconfCenterStore.getConfFileMap().containsKey(key)) {

            disconfCenterStore.getConfFileMap().get(key)
                    .getDisconfCommonCallbackModel().getDisconfConfUpdates()
                    .add(iDisconfUpdate);
        }
    }

    /**
     * 添加一个更新 的回调函数
     */
    public void addUpdateCallbackList(String key,
            List<IDisconfUpdate> iDisconfUpdateList) {

        if (disconfCenterStore.getConfFileMap().containsKey(key)) {

            disconfCenterStore.getConfFileMap().get(key)
                    .getDisconfCommonCallbackModel().getDisconfConfUpdates()
                    .addAll(iDisconfUpdateList);

        } else {

            if (disconfCenterStore.getConfItemMap().containsKey(key)) {

                disconfCenterStore.getConfItemMap().get(key)
                        .getDisconfCommonCallbackModel()
                        .getDisconfConfUpdates().addAll(iDisconfUpdateList);
            }
        }
    }

    /**
     * 获取配置文件数据，根据文件名及Key名获取数据
     * 
     * @param fileName
     * @param keyName
     * @return
     */
    public Object getConfigFile(String fileName, String keyName) {

        DisconfCenterFile disconfCenterFile = disconfCenterStore
                .getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.info("canot find " + fileName + " in store....");
            return null;
        }

        if (disconfCenterFile.getKeyMaps().get(keyName) == null) {
            LOGGER.info("canot find " + fileName + ", " + keyName
                    + " in store....");
            return null;
        }

        return disconfCenterFile.getKeyMaps().get(keyName).getValue();
    }

    /**
     * 获取配置项数据, 根据 KEy获取
     * 
     * @param keyName
     * @return
     */
    public Object getConfigItem(String keyName) {

        DisconfCenterItem disconfCenterItem = disconfCenterStore
                .getConfItemMap().get(keyName);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.info("canot find " + keyName + " in store....");
            return null;
        }

        return disconfCenterItem.getValue();
    }

    /**
     * 将配置文件数据注入到仓库
     */
    public void injectFile2Store(String fileName, Properties properties) {

        DisconfCenterFile disconfCenterFile = disconfCenterStore
                .getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.error("canot find " + fileName + " in store....");
            return;
        }

        // 存储
        Map<String, FileItemValue> keMap = disconfCenterFile.getKeyMaps();
        for (String fileItem : keMap.keySet()) {

            Object object = properties.get(fileItem);
            if (object == null) {
                continue;
            }

            // 根据类型设置值
            try {

                Object value = ClassUtils.getValeByType(keMap.get(fileItem)
                        .getField().getType(), (String) object);
                keMap.get(fileItem).setValue(value);

                // 如果Object非null,则顺便也注入
                if (disconfCenterFile.getObject() != null) {
                    keMap.get(fileItem)
                            .getField()
                            .set(disconfCenterFile.getObject(),
                                    keMap.get(fileItem).getValue());
                }

            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }
    }

    /**
     * 将配置项数据注入到仓库
     */
    public void injectItem2Store(String key, String value) {

        DisconfCenterItem disconfCenterItem = disconfCenterStore
                .getConfItemMap().get(key);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + key + " in store....");
            return;
        }

        // 存储
        Class<?> typeClass = disconfCenterItem.getField().getType();

        // 根据类型设置值
        //
        // 注入仓库
        //
        try {

            Object newValue = ClassUtils.getValeByType(typeClass, value);
            disconfCenterItem.setValue(newValue);

            // 如果Object非null,则顺便也注入
            if (disconfCenterItem.getObject() != null) {
                disconfCenterItem.getField().set(disconfCenterItem.getObject(),
                        disconfCenterItem.getValue());
            }

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
            return;
        }
    }

    /**
     * 将配置项数据注入实体
     */
    public void injectItem2Instance(String key) {

        DisconfCenterItem disconfCenterItem = disconfCenterStore
                .getConfItemMap().get(key);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + key + " in store....");
            return;
        }

        // 无实例无值则 无法注入
        if (disconfCenterItem.getObject() == null) {
            LOGGER.warn(key + " 's oboject is null");
            return;
        }

        // 根据类型设置值
        //
        // 注入实体
        //
        try {

            disconfCenterItem.getField().set(disconfCenterItem.getObject(),
                    disconfCenterItem.getValue());

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
            return;
        }
    }

    /*
     * 将配置文件数据注入实体
     */
    public void injectFileItem2Instance(String fileName) {

        DisconfCenterFile disconfCenterFile = disconfCenterStore
                .getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.error("canot find " + fileName + " in store....");
            return;
        }

        // 无实例无值则 无法注入
        if (disconfCenterFile.getObject() == null) {
            LOGGER.warn(fileName + " 's oboject is null");
            return;
        }

        // 根据类型设置值
        //
        // 注入实体
        //
        Map<String, FileItemValue> keMap = disconfCenterFile.getKeyMaps();
        for (String fileItem : keMap.keySet()) {

            // 根据类型设置值
            try {

                keMap.get(fileItem)
                        .getField()
                        .set(disconfCenterFile.getObject(),
                                keMap.get(fileItem).getValue());
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
            }
        }
    }

    /**
     * 获取配置文件在Zookeeper上的路径
     * 
     * @param fileName
     * @return
     */
    public String getFileZooPath(String fileName) {

        DisconfCenterFile disconfCenterFile = disconfCenterStore
                .getConfFileMap().get(fileName);

        // 校验是否存在
        if (disconfCenterFile == null) {
            LOGGER.error("canot find " + fileName + " in store....");
            return null;
        }

        return disconfCenterFile.getDisConfCommonModel().getZookeeperUrl();
    }

    /**
     * 获取配置项在ZK上的路径
     * 
     * @param fileName
     * @return
     */
    public String getItemZooPath(String key) {

        DisconfCenterItem disconfCenterItem = disconfCenterStore
                .getConfItemMap().get(key);

        // 校验是否存在
        if (disconfCenterItem == null) {
            LOGGER.error("canot find " + key + " in store....");
            return null;
        }

        return disconfCenterItem.getDisConfCommonModel().getZookeeperUrl();
    }
}