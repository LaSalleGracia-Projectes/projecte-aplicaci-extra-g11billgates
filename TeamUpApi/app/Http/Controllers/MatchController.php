<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\MatchUsers;
use App\Models\Like;

class MatchController extends Controller
{
    public function store(Request $request)
    {
        $request->validate([
            'IDUsuario1' => 'required|exists:users,id',
        ]);

        $match = MatchUsers::create([
            'IDUsuario1' => $request->IDUsuario1,
            'IDUsuario2' => auth()->id(),
            'FechaCreacion' => now(),
        ]);

        return response()->json([
            'status' => 'Match Created!',
            'match' => $match
        ]);
    }

    public function like(Request $request)
    {
        $request->validate([
            'IDUsuario2' => 'required|integer|exists:users,id',
        ]);

        $IDUsuario1 = auth()->id();

        $existe = Like::where('IDUsuario1', $IDUsuario1)
                    ->where('IDUsuario2', $request->IDUsuario2)
                    ->exists();

        if ($existe) {
            return response()->json([
                'message' => 'Ya le diste like a este usuario.'
            ], 409);
        }

        $like = Like::create([
            'IDUsuario1' => $IDUsuario1,
            'IDUsuario2' => $request->IDUsuario2,
            'Fecha' => now(),
        ]);

        return response()->json([
            'message' => 'Like registrado correctamente.',
            'like' => $like
        ], 201);
    }

    public function unlikeReceived(Request $request)
    {
        $request->validate([
            'IDUsuario1' => 'required|integer|exists:users,id',
        ]);

        $IDUsuario2 = auth()->id();

        $like = Like::where('IDUsuario1', $request->IDUsuario1)
                    ->where('IDUsuario2', $IDUsuario2)
                    ->first();

        if (!$like) {
            return response()->json([
                'message' => 'No existe like recibido por ti desde ese usuario.'
            ], 404);
        }

        $like->delete();

        return response()->json([
            'message' => 'Like eliminado correctamente.'
        ], 200);
    }

}
