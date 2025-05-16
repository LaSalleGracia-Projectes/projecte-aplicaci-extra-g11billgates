<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Juego;
use App\Models\Usuario;

class JuegoController extends Controller
{
    public function añadirJuego(Request $request)
    {
        $request->validate([
            'juego_id' => 'required|exists:juegos,IDJuego',
        ]);

        $user = auth()->user(); // ✅
        $user->juego()->syncWithoutDetaching([$request->juego_id]);


        return response()->json(['message' => 'Juego añadido al usuario.']);
    }

    public function borrarJuego(Request $request)
    {
        $request->validate([
            'juego_id' => 'required|exists:juego,IDJuego',
        ]);

        $user = auth()->user();

        $user->juego()->detach($request->juego_id);

        return response()->json(['message' => 'Juego eliminado del usuario.']);
    }

    public function index()
    {
        return response()->json(Juego::all());
    }
    public function juegosFavoritos($id)
    {
        $usuario = \App\Models\Usuario::with('juego')->find($id);

        if (!$usuario) {
            return response()->json(['error' => 'Usuario no encontrado'], 404);
        }

        $juegos = $usuario->juego->map(function ($juego) {
            return [
                'Nombre' => $juego->NombreJuego,
                'Genero' => $juego->Genero,
            ];
        });

        return response()->json($juegos);
    }


}
