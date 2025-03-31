<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Laravel\Sanctum\HasApiTokens;
use Illuminate\Foundation\Auth\User as Authenticatable;

class Usuario extends Model
{
    use HasFactory;
    use HasApiTokens;

    protected $fillabe = [
        'IDUsuario',
        'Nombre',
        'Correo',
        'Contraseña',
        'FotoPerfil',
        'Edad',
        'Region'
    ];
    protected $hidden = [
        'Contraseña'
    ];
    protected function cast(): array
    {
        return [
            'Contraseña' => 'hashed'
        ];
    }
    public function matchUsers()
    {
        return $this->hasMany(MatchUsers::class);
    }
    public function juego(): BelongsToMany
    {
        return $this->belongsToMany(Juego::class)->withPivot('Estadisticas', 'Preferencias', 'NivelElo');
    }
}
