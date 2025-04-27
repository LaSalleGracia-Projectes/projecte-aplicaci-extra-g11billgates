<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\MatchUsers;

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
            'FechaMatch' => now(),
        ]);

        return response()->json([
            'status' => 'Match Created!',
            'match' => $match
        ]);
    }
}
