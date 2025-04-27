<?php

namespace App\Http\Controllers;

use App\Events\MessageSent;
use App\Models\Mensaje;
use Illuminate\Http\Request;

class ChatController extends Controller
{
    public function index()
    {
        // Devuelve los últimos 50 mensajes (puedes ajustar)
        return Message::latest()->take(50)->get()->reverse()->values();
    }

    public function store(Request $request)
    {
        $request->validate([
            'IDChat' => 'required|exists:chats,IDChat',
            'IDUsuario' => auth()->user()->IDUsuario,
            'Tipo' => 'required|string',
            'FechaEnvio' => 'required|date',
        ]);

        $mensaje = Mensaje::create([
            'IDChat' => $request->IDChat,
            'IDUsuario' => $request->IDUsuario,
            'Tipo' => $request->Tipo,
            'FechaEnvio' => $request->FechaEnvio,
        ]);

        broadcast(new MessageSent($mensaje))->toOthers();

        return response()->json(['status' => 'Message Sent!']);
    }


}
