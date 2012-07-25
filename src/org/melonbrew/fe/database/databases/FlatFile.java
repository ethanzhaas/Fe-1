package org.melonbrew.fe.database.databases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.melonbrew.fe.Fe;
import org.melonbrew.fe.database.Account;
import org.melonbrew.fe.database.AccountCompare;
import org.melonbrew.fe.database.Database;

public class FlatFile extends Database {
	private final File storageFile;
	
	private final Properties storage;
	
	public FlatFile(Fe plugin){
		super(plugin);
		
		storageFile = new File(plugin.getDataFolder(), "flatdatabase.prop");
		
		storage = new Properties();
	}
	
	public boolean init(){
		try {
			storageFile.createNewFile();
		} catch (IOException e){
			e.printStackTrace();
			
			return false;
		}
		
		try {
			FileInputStream inputStream = new FileInputStream(storageFile);
			
			storage.load(inputStream);
		} catch (Exception e){
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	
	public void close(){
		
	}
	
	public double loadAccountMoney(String name){
		try {
			double money = Double.parseDouble(storage.getProperty(name));
			
			return money;
		} catch (Exception e){
			return -1;
		}
	}
	
	public void removeAccount(String name){
		storage.remove(name);
		
		saveFile();
	}
	
	private void saveFile(){
		storageFile.delete();
		
		try {
			storageFile.createNewFile();
			
			FileOutputStream outputStream = new FileOutputStream(storageFile);
			
			storage.store(outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void saveAccount(String name, double money){
		storage.setProperty(name, money + "");
		
		saveFile();
	}
	
	public List<Account> getTopAccounts(){
		List<Account> allAccounts = new ArrayList<Account>();
		
		for (String name : storage.stringPropertyNames()){
			Account account = getAccount(name);
			
			allAccounts.add(account);
		}
		
		Account[] array = allAccounts.toArray(new Account[allAccounts.size()]);
		
		Arrays.sort(array, new AccountCompare());
		
		List<Account> finalAccounts = new ArrayList<Account>();
		
		for (int i = array.length - 1; i > array.length - 6; i--){
			try {
				finalAccounts.add(array[i]);
			}catch (ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		
		return finalAccounts;
	}
}