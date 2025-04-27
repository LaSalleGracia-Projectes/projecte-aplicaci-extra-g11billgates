<?php

namespace App\Http\Controllers;

use App\Events\MessageSent;
use App\Models\Message;
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
            'content' => 'required|string|max:1000',
        ]);

        // Crear el mensaje
        $message = Message::create([
            'user_id' => auth()->id(), // o manejar de otro modo si no hay auth
            'content' => $request->content,
        ]);

        // Emitir evento de mensaje enviado
        broadcast(new MessageSent($message))->toOthers();

        return response()->json(['status' => 'Message Sent!']);
    }
}
