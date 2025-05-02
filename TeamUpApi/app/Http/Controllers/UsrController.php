<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use App\Models\Usuario;

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
            'foto' => 'required|image|max:2048', 
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
    public function getUserById($id)
    {
        $usuario = Usuario::find($id);

        if (!$usuario) {
            return response()->json([
                'message' => 'Usuario no encontrado.'
            ], 404);
        }

        return response()->json($usuario);
    }
    public function getRandomUserId(Request $request)
    {
        $excludedId = auth()->id();

        // Obtener todos los IDs válidos desde el 6 en adelante, excluyendo el actual
        $ids = Usuario::where('id', '>=', 6)
                    ->when($excludedId, fn($q) => $q->where('id', '!=', $excludedId))
                    ->pluck('id')
                    ->toArray();

        if (empty($ids)) {
            return response()->json(['message' => 'No hay usuarios disponibles.'], 404);
        }

        // Seleccionar uno aleatoriamente
        $randomId = $ids[array_rand($ids)];

        return response()->json(['random_user_id' => $randomId]);
    }

}
