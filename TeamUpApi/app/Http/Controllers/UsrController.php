<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class UsrController extends Controller
{
    public function index()
    {
        return response()->json([
            'status' => 'success',
            'message' => 'Users retrieved successfully',
            'data' => Usuario::all()
        ], 200);
    }
    public function subirFotoPerfil(Request $request)
    {
        $request->validate([
            'foto' => 'required|image|max:2048', // Máx. 2MB
        ]);

        $user = auth()->user();

        // Guarda en storage/app/public/perfiles/
        $ruta = $request->file('foto')->store('perfiles', 'public');

        // Guarda la ruta relativa en el campo FotoPerfil
        $user->FotoPerfil = $ruta;
        $user->save();

        return response()->json([
            'message' => 'Foto de perfil actualizada correctamente.',
            'foto_url' => asset('storage/' . $ruta),
        ]);
    }
}
