<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Chat extends Model
{
    use HasFactory;

    protected $table = 'chats';
    public $timestamps = false;
    protected $primaryKey = 'IDChat';

    protected $fillable = [
        'IDChat',
        'IDMatch',
        'FechaCreacion',
    ];

    public function matchUser()
    {
        return $this->belongsTo(MatchUsers::class, 'IDMatch', 'IDMatch');
    }

    /**
     * Relación a mensajes 1:n
     */
    public function mensajes()
    {
        return $this->hasMany(Mensaje::class, 'IDChat', 'IDChat');
    }
}
