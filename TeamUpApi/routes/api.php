<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ChatController;
use App\Http\Controllers\MatchController;
use App\Http\Middleware\RegisterMiddleware;
use App\Http\Middleware\LoginMiddleware;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::post('register', [AuthController::class, 'register'])->middleware(RegisterMiddleware::class);
Route::post('login', [AuthController::class, 'login'])->middleware(LoginMiddleware::class);

//Rutas de chat
Route::get('/messages', [ChatController::class, 'index'])->middleware('auth:sanctum');
Route::post('/messages', [ChatController::class, 'store'])->middleware('auth:sanctum');
Route::post('/chats', [ChatController::class, 'storeChat'])->middleware('auth:sanctum');
