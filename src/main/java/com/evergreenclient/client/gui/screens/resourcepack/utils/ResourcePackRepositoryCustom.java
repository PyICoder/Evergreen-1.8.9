/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui.screens.resourcepack.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.evergreenclient.client.utils.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.settings.GameSettings;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class ResourcePackRepositoryCustom extends ResourcePackRepository {

    public static void overrideRepository(List<String> enabledPacks){
        Minecraft mc = Minecraft.getMinecraft();

        Field fieldRepository = ReflectionHelper.findField(Minecraft.class, "field_110448_aq", "mcResourcePackRepository");
        File fileResourcepacks = ReflectionHelper.getPrivateValue(Minecraft.class, mc, "field_130070_K","fileResourcepacks");

        try{
            ResourcePackRepository originalRepo = (ResourcePackRepository)fieldRepository.get(mc);
            fieldRepository.set(
                    mc,
                    new ResourcePackRepositoryCustom(
                            fileResourcepacks,
                            new File(mc.mcDataDir,"server-resource-packs"),
                            (IResourcePack) ReflectionHelper.findField(
                                    Minecraft.class, "field_110450_ap", "mcDefaultResourcePack")
                                    .get(mc),
                            originalRepo.rprMetadataSerializer,
                            mc.gameSettings,enabledPacks));
        }catch(Throwable t){
            throw new RuntimeException(t);
        }
    }

    public static Entry createEntryInstance(ResourcePackRepository repository, File file){
        try{
            if (entryConstructor == null){
                entryConstructor = Entry.class.getDeclaredConstructor(ResourcePackRepository.class,File.class);
                entryConstructor.setAccessible(true);
            }

            return entryConstructor.newInstance(repository,file);
        }catch(Throwable t){
            t.printStackTrace();
            return null;
        }
    }

    private static Constructor<Entry> entryConstructor;

    private List<Entry> repositoryEntriesAll = Lists.newArrayList();
    private List<Entry> repositoryEntries = Lists.newArrayList();
    private boolean isReady;

    public ResourcePackRepositoryCustom(File dirResourcepacks, File dirServerResourcepacks, IResourcePack rprDefaultResourcePack, IMetadataSerializer rprMetadataSerializer, GameSettings settings, List<String> enabledPacks){
        super(dirResourcepacks,dirServerResourcepacks,rprDefaultResourcePack,rprMetadataSerializer,settings);

        isReady = true;
        updateRepositoryEntriesAll();

        for(String pack:enabledPacks){
            for(Entry entry:repositoryEntriesAll){
                if (entry.getResourcePackName().equals(pack)){
                    repositoryEntries.add(entry); // removed incompatible resource pack removal
                }
            }
        }
    }

    private List<File> getResourcePackFiles(File root){
        if (root.isDirectory()){
            List<File> packFiles = Lists.newArrayList();

            for(File file:root.listFiles()){
                if (file.isDirectory() && !new File(file,"pack.mcmeta").isFile()){
                    packFiles.addAll(getResourcePackFiles(file));
                }
                else{
                    packFiles.add(file);
                }
            }

            return packFiles;
        }
        else{
            return Collections.emptyList();
        }
    }

    @Override
    public void updateRepositoryEntriesAll(){
        if (!isReady)return;

        List<Entry> list = Lists.newArrayList();

        for(File file:getResourcePackFiles(getDirResourcepacks())){
            Entry entry = createEntryInstance(this,file);

            if (!repositoryEntriesAll.contains(entry)){
                try{
                    entry.updateResourcePack();
                    list.add(entry);
                }catch(Exception e){
                    list.remove(entry);
                }
            }
            else{
                int index = repositoryEntriesAll.indexOf(entry);

                if (index > -1 && index < repositoryEntriesAll.size()){
                    list.add(repositoryEntriesAll.get(index));
                }
            }
        }

        repositoryEntriesAll.removeAll(list);

        for(ResourcePackRepository.Entry entry:repositoryEntriesAll){
            entry.closeResourcePack();
        }

        repositoryEntriesAll = list;
    }

    @Override
    public List<Entry> getRepositoryEntriesAll(){
        return ImmutableList.copyOf(repositoryEntriesAll);
    }

    @Override
    public List<Entry> getRepositoryEntries(){
        return ImmutableList.copyOf(repositoryEntries);
    }

    @Override
    public void setRepositories(List<Entry> repositories){
        repositoryEntries.clear();
        repositoryEntries.addAll(repositories);
    }

}
