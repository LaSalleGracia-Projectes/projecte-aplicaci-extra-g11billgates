<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ChatController;
use App\Http\Controllers\MatchController;
use App\Http\Controllers\UsrController;
use App\Http\Controllers\JuegoController;
use App\Http\Middleware\RegisterMiddleware;
use App\Http\Middleware\LoginMiddleware;
use App\Http\Middleware\EnsureUserInChatMatch;

//Rutas de usuario
Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');
Route::post('/user/foto-perfil', [UsrController::class, 'subirFotoPerfil'])->middleware('auth:sanctum');
Route::get('/usuario/{id}', [UsrController::class, 'getUserById'])->middleware('auth:sanctum');
Route::get('/usuarios/aleatorio', [UsrController::class, 'getRandomUserId'])->middleware('auth:sanctum');
Route::delete('/usuarios/{id}', [UsrController::class, 'destroy'])->middleware('auth:sanctum');

//Login y register
Route::post('register', [AuthController::class, 'register'])->middleware(RegisterMiddleware::class);
Route::post('login', [AuthController::class, 'login'])->middleware(LoginMiddleware::class);

//Rutas de chat
Route::get('/messages', [ChatController::class, 'index'])->middleware('auth:sanctum');
Route::post('/messages', [ChatController::class, 'store'])->middleware('auth:sanctum');
Route::post('/chats', [ChatController::class, 'storeChat'])->middleware('auth:sanctum');
//ruta para ver un chat concreto con todos sus mensajes
Route::get('/chats/{idChat}/messages', [ChatController::class, 'getMessagesFromChat'])
    ->middleware(['auth:sanctum', EnsureUserInChatMatch::class]);
Route::delete('/chats/{id}', [ChatController::class, 'destroy'])->middleware('auth:sanctum');
Route::get('/listchats', [ChatController::class, 'listChats'])
    ->middleware('auth:sanctum');
Route::get('/chat/{id}', [ChatController::class, 'show'])->middleware('auth:sanctum');


//rutas de match
Route::post('/matches', [MatchController::class, 'store'])->middleware('auth:sanctum');
Route::post('/likes', [MatchController::class, 'like'])->middleware('auth:sanctum');
Route::delete('/likes/received', [MatchController::class, 'unlikeReceived'])->middleware('auth:sanctum');
Route::post('/match/check', [MatchController::class, 'checkMutualLike'])->middleware('auth:sanctum');
Route::delete('/match', [MatchController::class, 'deleteMatch'])->middleware('auth:sanctum');

//Rutas de juego
Route::post('/juego', [JuegoController::class, 'añadirJuego'])->middleware('auth:sanctum');
Route::delete('/juego', [JuegoController::class, 'borrarJuego'])->middleware('auth:sanctum');
Route::get('/juegos', [JuegoController::class, 'index'])->middleware('auth:sanctum');
