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
        ]);

        // 👇 IDUsuario viene del usuario logueado, no del request
        $mensaje = Mensaje::create([
            'IDChat' => $request->IDChat,
            'IDUsuario' => auth()->id(),
            'Tipo' => $request->Tipo,
            'FechaEnvio' => $request->FechaEnvio,
        ]);

        // Emite el evento
        broadcast(new MessageSent($mensaje))->toOthers();

        return response()->json(['status' => 'Message Sent!']);
    }
}
