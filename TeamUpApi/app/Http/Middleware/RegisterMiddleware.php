<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;
use Illuminate\Support\Facades\Validator;

class RegisterMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Closure(\Illuminate\Http\Request): (\Symfony\Component\HttpFoundation\Response)  $next
     */
    public function handle(Request $request, Closure $next): Response
    {
        $validator = Validator::make($request->all(), [
            'Nombre' => 'required|string|max:20|unique:users,Nombre',
            'email' => 'required|string|email|unique:users,email',
            'password' => 'required|string|min:8|regex:/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).+$/|confirmed',
            'Edad' => 'required|integer|min:18',
            'Region' => 'required|string'

        ], [
            'Nombre.required'      => 'El nombre de usuario es obligatorio.',
            'Nombre.max'           => 'El nombre de usuario tiene que tener un máximo de 20 carácteres',
            'Nombre.unique'        => 'Este nombre de usuario ya existe',
            'email.required'      => 'El correo es obligatorio.',
            'email.email'         => 'El correo debe ser un email válido.',
            'email.unique'        => 'Este correo ya está registrado.',
            'password.required'  => 'La contraseña es obligatoria.',
            'password.min'       => 'La contraseña debe tener al menos 8 caracteres.',
            'password.regex'     => 'La contraseña ha de tener almenos una mayúscula, una minúscula, un numero y un simbolo (@, #, $, % etc)',
            'password.confirmed' => 'Las contraseñas no coinciden.',
            'Edad.required'        => 'La edad es obligatoria.',
            'Edad.integer'         => 'La edad debe ser un número entero.',
            'Edad.min'             => 'Debes tener al menos 18 años para usar esta aplicación.',
            'region.required'      => 'La región es obligatoria.',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'errors' => $validator->errors(),
            ], 422);
        }
        return $next($request);
    }
}
