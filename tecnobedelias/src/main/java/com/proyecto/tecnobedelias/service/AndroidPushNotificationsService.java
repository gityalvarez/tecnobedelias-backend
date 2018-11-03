package com.proyecto.tecnobedelias.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;

public interface AndroidPushNotificationsService {
	
	CompletableFuture<String> send(HttpEntity<String> entity);

}
