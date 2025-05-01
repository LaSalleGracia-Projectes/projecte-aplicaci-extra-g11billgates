<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ChatController;
use App\Http\Controllers\MatchController;
use App\Http\Middleware\RegisterMiddleware;
use App\Http\Middleware\LoginMiddleware;
use App\Http\Middleware\EnsureUserInChatMatch;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::post('register', [AuthController::class, 'register'])->middleware(RegisterMiddleware::class);
Route::post('login', [AuthController::class, 'login'])->middleware(LoginMiddleware::class);

//Rutas de chat
Route::get('/messages', [ChatController::class, 'index'])->middleware('auth:sanctum');
Route::post('/messages', [ChatController::class, 'store'])->middleware('auth:sanctum');
Route::post('/chats', [ChatController::class, 'storeChat'])->middleware('auth:sanctum');
//ruta para ver un chat concreto con todos sus mensajes
Route::get('/chats/{idChat}/messages', [ChatController::class, 'getMessagesFromChat'])
    ->middleware(['auth:sanctum', EnsureUserInChatMatch::class]);


//rutas de match
Route::post('/matches', [MatchController::class, 'store'])->middleware('auth:sanctum');
Route::post('/likes', [MatchController::class, 'like'])->middleware('auth:sanctum');
Route::delete('/likes/received', [MatchController::class, 'unlikeReceived'])->middleware('auth:sanctum');
