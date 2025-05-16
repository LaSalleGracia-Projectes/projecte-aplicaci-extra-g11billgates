<?php


namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Usuario;
use App\Models\User;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        $user = Usuario::create([
            'Nombre' => $request->Nombre,
            'email' => $request->email,
            'password' => bcrypt($request->password),
            'Edad' => $request->Edad,
            'Region' => $request->Region
        ]);

        return response()->json([
            'user' => $user,
            'status' => 201
        ]);
    }

    public function login(Request $request)
    {
        if (!Auth::attempt($request->only('email', 'password'))) {
            return response(['error' => 'Credentials not match'], 401);
        }
        $token = auth()->user()->createToken('Auth token');
        $res = [
            'user' => auth()->user(),
            'token' => $token,
            'plain' => $token->plainTextToken
        ];
        return response()->json($res);
    }
}
