<?php


namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        $user = User::create([
            'Nombre' => $request->Nombre,
            'Correo' => $request->Correo,
            'Contraseña' => bcrypt($request->Contraseña)
        ]);
        return response()->json([$user, 201]);
    }
    public function login(Request $request)
    {
        if (!Auth::attempt($request->only('Nombre', 'Contraseña'))) {
            return response(['error' => 'Credentials not match'], 401);
        }
        $token = auth()->user()->createToken('Auth token');
        $res = [
            'token' => $token,
            'plain' => $token->plainTextToken
        ];
        return response()->json($res);
    }
}
