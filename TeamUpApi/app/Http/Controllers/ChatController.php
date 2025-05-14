<?php

namespace App\Http\Controllers;

use App\Events\MessageSent;
use App\Models\Mensaje;
use App\Models\Chat;
use App\Models\MatchUsers;
use Illuminate\Http\Request;

class ChatController extends Controller
{
    public function index()
    {
        return Mensaje::latest('FechaEnvio')->take(50)->get()->reverse()->values();
    }

    public function store(Request $request)
    {
        $request->validate([
            'IDChat' => 'required|exists:chats,IDChat',
            'Tipo' => 'required|string',
            'FechaEnvio' => 'required|date',
            'Texto' => 'nullable|string|max:5000',
        ]);

        $mensaje = Mensaje::create([
            'IDChat' => $request->IDChat,
            'IDUsuario' => auth()->id(),
            'Tipo' => $request->Tipo,
            'FechaEnvio' => $request->FechaEnvio,
            'Texto' => $request->Texto,
        ]);

        broadcast(new MessageSent($mensaje))->toOthers();

        return response()->json(['status' => 'Message Sent!']);
    }

    public function storeChat(Request $request)
    {
        $request->validate([
            'IDMatch' => 'required|exists:match_users,IDMatch',
        ]);

        $chat = Chat::create([
            'IDMatch' => $request->IDMatch,
            'FechaCreacion' => now(),
        ]);

        return response()->json([
            'status' => 'Chat Created!',
            'chat' => $chat
        ]);
    }


    public function getMessagesFromChat($idChat)
    {
        $mensajes = Mensaje::where('IDChat', $idChat)
                    ->orderBy('FechaEnvio', 'asc')
                    ->get();

        return response()->json([
            'mensajes' => $mensajes
        ]);
    }

    public function destroy($id)
    {
        $userId = auth()->id();

        $chat = Chat::find($id);

        if (!$chat) {
            return response()->json(['message' => 'Chat no encontrado.'], 404);
        }

        $match = MatchUsers::find($chat->IDMatch);

        if (!$match || ($match->IDUsuario1 !== $userId && $match->IDUsuario2 !== $userId)) {
            return response()->json(['message' => 'No tienes permiso para eliminar este chat.'], 403);
        }

        $chat->delete();

        return response()->json(['message' => 'Chat eliminado correctamente.'], 200);
    }
    public function listChats(Request $request)
    {
        $userId = auth()->id();

        $chats = Chat::whereHas('matchUser', function ($q) use ($userId) {
            $q->where('IDUsuario1', $userId)
              ->orWhere('IDUsuario2', $userId);
        })->get();

        return response()->json([
            'chats' => $chats
        ], 200);
    }
}
