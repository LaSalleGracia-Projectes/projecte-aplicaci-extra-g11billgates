<?php

namespace App\Http\Controllers;

use App\Events\MessageSent;
use App\Models\Mensaje;
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
            'FechaCreacion' => now(), // 👈 Fecha automática
        ]);

        return response()->json([
            'status' => 'Chat Created!',
            'chat' => $chat
        ]);
    }

}
